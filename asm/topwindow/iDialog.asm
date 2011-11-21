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
 HOOK_INIT_MAGIC     equ 0abcdefh
 IDD_MSGSENDER_DIALOG		equ		102
 DLG_MAIN                          equ    1000
ICO_MAIN                         equ    1000
ID_TEST                           equ     1002
 _PROCVAR0 typedef proto
 _PROCVAR1 typedef proto: dword

  PROCVAR0 typedef ptr _PROCVAR0
PROCVAR1 typedef ptr _PROCVAR1
; ###########################################################################
.data
hInstance dd 0
hIMenuDll dd 0
lpGetHookStatus PROCVAR0 0
lpInstallHook      PROCVAR1 0
lpUnInstallHook  PROCVAR0 0

szFailedmsg db "GetProcAddress", 0
szCaption     db  'Arthur said', 0



.const
szDLLName    db  './/iMenu.dll', 0
szInstallHook  db 'InstallHook', 0
szUnInstallHook db 'UnInstallHook', 0
szGetHookStatus db 'Astub', 0
szInvokeDialog    db  'try to start dialog', 0

; ###########################################################################
.code
EchoLastErr proc
    local @lpMsg : LPVOID
    local @error 
    invoke GetLastError
    mov @error, eax    
    invoke FormatMessage,  FORMAT_MESSAGE_ALLOCATE_BUFFER or   FORMAT_MESSAGE_FROM_SYSTEM or  FORMAT_MESSAGE_IGNORE_INSERTS, \
        NULL,    @error,       0,       addr @lpMsg,        0,        NULL 
    invoke MessageBox, NULL, @lpMsg, offset szCaption, MB_OK
    invoke LocalFree, @lpMsg
    ret
EchoLastErr endp

ProcDlgMain1 proc hWnd, wMsg, wParam, lParam
     .if eax == WM_CLOSE       
        invoke EndDialog, hWnd, NULL
   .elseif eax == WM_INITDIALOG
        invoke MessageBox, hWnd, offset szFailedmsg, offset szCaption, MB_OK
        invoke LoadIcon, hInstance, ICO_MAIN
        invoke SendMessage, hWnd, WM_SETICON, ICON_BIG, eax
        invoke SetDlgItemText, hWnd, ID_TEST,  offset szUnInstallHook
        
   .elseif eax == WM_COMMAND
        mov eax, wParam
        .if ax == IDCANCEL
                   invoke EndDialog, hWnd, NULL
        .elseif ax == IDOK
            invoke MessageBox, hWnd, offset szInvokeDialog, offset szCaption, MB_OK
        .endif
        
   .elseif
        mov eax, FALSE
        ret
   .endif
    mov eax, TRUE
   ret
   
ProcDlgMain1 endp

installhook proc hWnd
       invoke  LoadLibrary, offset szDLLName
       .if ! eax       
           invoke EchoLastErr
           ret
       .elseif
            mov hIMenuDll, eax
       .endif 

        invoke GetProcAddress, hIMenuDll, offset szInstallHook
        .if ! eax
            ;invoke MessageBox, hWnd, offset szFailedmsg, offset szCaption, MB_OK
            invoke EchoLastErr
            ret
        .endif
        mov lpInstallHook, eax        
        
        invoke GetProcAddress, hIMenuDll, offset szUnInstallHook
        .if ! eax 
             invoke EchoLastErr
             invoke MessageBox, hWnd, offset szUnInstallHook, offset szCaption, MB_OK
            ret           
        .endif     
        mov lpUnInstallHook, eax
        
        invoke GetProcAddress,  hIMenuDll, offset szGetHookStatus
        .if ! eax 
            invoke EchoLastErr
            invoke MessageBox, hWnd, offset szGetHookStatus, offset szCaption, MB_OK
            ret           
        .endif     
        mov lpGetHookStatus, eax
        ret
installhook endp

_ProcDlgMain proc hWnd, wMsg, wParam, lParam   
   ; invoke MessageBox, hWnd, offset szFailedmsg, offset szCaption, MB_OK
    mov eax, wMsg
    .if wMsg == WM_CLOSE
        .if hIMenuDll != NULL
            invoke FreeLibrary, hIMenuDll
        .endif
        invoke EndDialog, hWnd, NULL
   .elseif wMsg == WM_INITDIALOG
        invoke LoadIcon, hInstance, ICO_MAIN
        invoke SendMessage, hWnd, WM_SETICON, ICON_BIG, eax
        invoke installhook, hWnd
        ;invoke MessageBox, hWnd, offset szFailedmsg, offset szCaption, MB_OK
        ; 
        .if lpGetHookStatus
            invoke lpGetHookStatus
            .if eax != 0
                invoke SetDlgItemText, hWnd, ID_TEST,  offset szInstallHook
            .else
                invoke SetDlgItemText, hWnd, ID_TEST, offset szUnInstallHook
                invoke lpUnInstallHook
            .endif
        .endif
    .elseif  wMsg == WM_COMMAND
        mov eax, wParam
        .if ax == IDCANCEL
            invoke EndDialog, hWnd, NULL
            .if hIMenuDll != NULL
                invoke FreeLibrary, hIMenuDll
            .endif
        .elseif ax == ID_TEST
             invoke lpGetHookStatus
              .if eax != 0
                invoke GetCurrentThreadId
                invoke lpInstallHook, eax
                .if eax !=0
                    invoke EchoLastErr
                 .elseif 
                     invoke SetDlgItemText, hWnd, ID_TEST, offset szUnInstallHook
                 .endif
              .elseif
                invoke lpUnInstallHook
                invoke SetDlgItemText, hWnd, ID_TEST, offset szInstallHook
              .endif
        .endif    
   .else 
        mov eax, FALSE
        ret
   .endif  
    mov eax, TRUE
    ret        
_ProcDlgMain endp



; >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
start:
    invoke GetModuleHandle, NULL
    mov hInstance, eax
    ;invoke MessageBox, NULL, offset szInvokeDialog, offset szCaption, MB_OK
    invoke DialogBoxParam, hInstance, DLG_MAIN, NULL, offset _ProcDlgMain,NULL
    invoke ExitProcess, NULL
; >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    end start