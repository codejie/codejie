.386 
        .model   flat,   stdcall 
        option   casemap   :none       ;   case   sensitive 
; http://code.google.com/p/lingoshook/
;   ######################################################################### 

        include   \masm32\include\windows.inc 
        include   \masm32\include\user32.inc 
        include   \masm32\include\kernel32.inc 

        includelib   \masm32\lib\user32.lib 
        includelib   \masm32\lib\kernel32.lib 

;   ######################################################################### 
 

; ###########################################################################
; definations
WM_TOPWIN              equ 000DDDDh
WM_NOTOP              equ 000DDDEh

HOOK_INIT_MAGIC     equ 0abcdefh

; ###########################################################################
.data
hLocalWnd dd 0
hThisDLL     dd 0 

.const 
szSetWindowError db "SetWindowPos failed", 0
szSetWindowInfo db "SetWindowPos Successed", 0
szMBCaption db "tstdll 's   LibMain   Function ", 0
szAttachInfo db "PROCESS_ATTACH ",0
szDettachInfo db "THREAD_DETACH ",0 

szTOPWIN db "Top Window", 0
szNOTOP db "Cancel Top", 0
szInstallWndProcHookError db "Install WndProc Hook failed ", 0
szInstallMsgProcHookError  db "Install MsgProc HOOK failed" , 0
	
.data?
hHookWndProc dd ?
hHookMsgProc  dd ?
dwHookInit       dd ?
hThreadId       dd ?

;   ########################################################################## 

szText   MACRO   Name,   Text:VARARG 
      ;LOCAL   lbl 
      jmp   @f
       Name   db   Text,0 
      @@ : 
  ENDM 

 m2m   MACRO   M1,   M2 
       push   M2 
       pop     M1 
ENDM 

return   MACRO   arg 
       mov   eax,   arg 
       ret 
ENDM 

.code

	      ;szText szAttachInfo, "Attach Process"
	      ;szText szMBCaption, "Arthur's DLL"
	      ;szText szDettachInfo, "Dettach Process"
	      ;szText szSetWindowError, "Set Winodws Pos failed"
	      	
	      	;szText szTOPWIN, "Top Window"
             ;szText szInstallWndProcHookError, "Install WndProc Hook failed "
             ;szText szInstallMsgProcHookError, "Install MsgProc HOOK failed"
             
LibMain   proc   hInstDLL:DWORD,   reason:DWORD,   unused:DWORD                 
	      assume fs:nothing

	      mov EAX, reason
                .if   EAX   ==   DLL_PROCESS_ATTACH 
                        push hInstDLL
                        pop  hThisDLL
                        ; invoke   MessageBox, NULL, offset  szAttachInfo, offset  szMBCaption,MB_OK   
                .elseif   EAX   ==   DLL_THREAD_DETACH 
                        ;invoke   MessageBox, NULL, offset   szDettachInfo , offset   szMBCaption, MB_OK 
                        ;return TRUE
                .endif 
		return TRUE
LibMain   Endp 


; #####################################################################
; LRESULT CALLBACK SysMsgProc(
; int nCode,      // message flag
; WPARAM wParam,  // not used
;  LPARAM lParam   // message data
;);
;WindowsFromPoint(Point pt)  返回包含指定点得hwnd

;BOOL SetWindowPos(
 ; HWND hWnd,             // handle to window
;  HWND hWndInsertAfter,  // placement-order handle   HWND_TOPMOST
;  int X,                 // horizontal position
;  int Y,                 // vertical position
;  int cx,                // width
;  int cy,                // height
;  UINT uFlags            // window-positioning options  SWP_NOMOVE  SWP_NOREDRAW
;);

GlobalMenuProc proc uses ebx nCode: DWORD , wParam : WPARAM,  lpMsg:  LPARAM
      invoke CallNextHookEx, hHookWndProc, nCode, wParam, lpMsg
      pushad

	mov ebx, lpMsg
	assume ebx: ptr MSG
	
	invoke GetWindowThreadProcessId, [ebx].hwnd, NULL

	;.if nCode == MSGF_MENU && [ebx].message == WM_TOPWIN
	.if  [ebx].wParam == WM_TOPWIN
		;
		invoke SetWindowPos, [ebx].hwnd,  HWND_TOPMOST,  NULL, NULL, NULL, NULL,  SWP_NOMOVE or SWP_NOSIZE or SWP_SHOWWINDOW
		
		.if ! EAX
			invoke MessageBox, NULL, offset szSetWindowError , offset  szMBCaption,   MB_OK 
		.endif		
       .elseif  [ebx].wParam == WM_NOTOP
		;
		invoke SetWindowPos, [ebx].hwnd,  HWND_NOTOPMOST,  NULL, NULL, NULL, NULL,  SWP_NOMOVE or SWP_NOSIZE or SWP_SHOWWINDOW
		.if ! EAX
			invoke MessageBox, NULL, offset szSetWindowError , offset  szMBCaption,   MB_OK 
		.endif
	.endif
	Assume ebx: nothing

	popad
      ret
GlobalMenuProc Endp
		
; ####################################################################
; LRESULT CALLBACK WindowProc(
;  HWND hwnd,       // handle to window
;  UINT uMsg,       // WM_INITMENU
;  WPARAM wParam,   // handle to menu (HMENU)
;  LPARAM lParam    // not used
; );


GlobalMenuAdd Proc uses ebx nCode: DWORD , wParam : WPARAM,  lpMsg:  LPARAM
	local @hSysMenu : DWORD
	local @hWnd : DWORD	
      invoke CallNextHookEx, hHookWndProc, nCode, wParam, lpMsg
      pushad

      .if nCode == HC_ACTION
	     mov ebx, lpMsg
	     Assume ebx: ptr CWPRETSTRUCT
	     m2m @hWnd, [ebx].hwnd
	    .if  [ebx].message == WM_INITMENU 
	       
		; record the hwnd
		m2m hLocalWnd , @hWnd
		invoke GetSystemMenu, @hWnd, FALSE
		mov @hSysMenu, EAX
		;mov EAX, WM_TOPWIN		
		invoke GetMenuState, @hSysMenu, WM_TOPWIN, MF_BYCOMMAND
		.if eax == -1
		    invoke InsertMenu, @hSysMenu, SC_SIZE, MF_BYCOMMAND or MF_STRING, WM_NOTOP, offset szNOTOP
		    invoke InsertMenu, @hSysMenu, SC_SIZE, MF_BYCOMMAND or MF_STRING, WM_TOPWIN, offset szTOPWIN
		.endif
		Assume ebx: nothing	
	    .endif	
	.endif
       popad  
	ret
GlobalMenuAdd endp
		
	
InstallHook Proc dwThreadId : DWORD
	;local @hDLL
	local @lasterr : DWORD
	m2m hThreadId, dwThreadId
	.if dwHookInit != HOOK_INIT_MAGIC
		;
		invoke SetWindowsHookEx, WH_CALLWNDPROCRET, offset GlobalMenuAdd, hThisDLL, NULL
		.if ! EAX
		      invoke GetLastError
		      mov @lasterr, eax
			invoke MessageBox, NULL, offset szInstallWndProcHookError , offset szMBCaption, MB_OK
			jmp @f
		.endif
		mov hHookWndProc, EAX
		invoke SetWindowsHookEx, WH_GETMESSAGE,  offset GlobalMenuProc, hThisDLL, NULL
		.if ! EAX
		      	invoke GetLastError
		      mov @lasterr, eax
			invoke UnhookWindowsHookEx, hHookWndProc
			invoke MessageBox, NULL, offset szInstallWndProcHookError , offset szMBCaption, MB_OK
			jmp @f
		.endif
		mov hHookMsgProc, EAX
		push HOOK_INIT_MAGIC
		pop dwHookInit
	.endif 
	; xor EAX, EAX
	return 0
@@ : 
      invoke SetLastError, @lasterr
      return -1
InstallHook endP


UnInstallHook Proc
	.if dwHookInit == HOOK_INIT_MAGIC
		invoke UnhookWindowsHookEx, hHookMsgProc
		invoke UnhookWindowsHookEx, hHookWndProc
		mov dwHookInit, 0
	.endif
	ret
UnInstallHook endp

GetHookStatus Proc
	.if dwHookInit == HOOK_INIT_MAGIC
		return 0
	.else 
		return -1
	.endif
	ret
GetHookStatus endp


SaveMasterThreadId Proc threadId: DWORD
      nop
	ret
SaveMasterThreadId endp


Astub Proc
	.if dwHookInit == HOOK_INIT_MAGIC
		return 0
	.else 
		return -1
	.endif
	ret
Astub endp
;>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	End LibMain