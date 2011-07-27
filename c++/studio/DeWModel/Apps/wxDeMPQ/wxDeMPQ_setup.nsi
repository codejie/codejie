; example2.nsi
;
; This script is based on example1.nsi, but it remember the directory, 
; has uninstall support and (optionally) installs start menu shortcuts.
;
; It will install example2.nsi into a directory that the user selects,

;--------------------------------

; The name of the installer
Name "wxDeMPQ"

; The file to write
OutFile "wxDeMPQ_debug_setup.exe"

; The default installation directory
InstallDir $PROGRAMFILES\Jie\wxDeMPQ

; Registry key to check for directory (so if you install again, it will 
; overwrite the old one automatically)
InstallDirRegKey HKLM "Software\Jie\wxDeMPQ" "Install_Dir"

; Request application privileges for Windows Vista
RequestExecutionLevel admin

;--------------------------------

; Pages

Page components
Page directory
Page instfiles

UninstPage uninstConfirm
UninstPage instfiles

;--------------------------------

; The stuff to install
Section "wxDeMPQ(d) (required)"

  SectionIn RO
  
  ; Set output path to the installation directory.
  SetOutPath $INSTDIR
  
  ; Put file there
  File ".\Debug\wxDeMPQ.exe"
	File ".\Debug\BLP2PNGLibraryd.dll"
	File ".\Debug\DSoundLibraryd.dll"
	File ".\Debug\ModelLibraryd.dll"
	File ".\Debug\DBQueryLibraryd.dll"
	File ".\DBCFields.xml"
	File ".\WDBFields.xml"
;	File "D:\Programs\vcredist_x86.exe"
  
  ; Write the installation path into the registry
  WriteRegStr HKLM SOFTWARE\Jie\wxDeMPQ "Install_Dir" "$INSTDIR"
  
  ; Write the uninstall keys for Windows
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\wxDeMPQ" "DisplayName" "wxDeMPQ(debug)"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\wxDeMPQ" "UninstallString" '"$INSTDIR\uninstall.exe"'
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\wxDeMPQ" "NoModify" 1
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\wxDeMPQ" "NoRepair" 1
  WriteUninstaller "uninstall.exe"
	
	CreateDirectory $INSTDIR\cache

SectionEnd

;Section "Install vcredist patch"
;	ExecWait "$INSTDIR\vcredist_x86.exe"
;SectionEnd

; Optional section (can be disabled by the user)
Section "Start Menu Shortcuts"

  CreateDirectory "$SMPROGRAMS\wxDeMPQ"
  CreateShortCut "$SMPROGRAMS\wxDeMPQ\Uninstall.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\uninstall.exe" 0
  CreateShortCut "$SMPROGRAMS\wxDeMPQ\wxDeMPQ.lnk" "$INSTDIR\wxDeMPQ.exe" "" "$INSTDIR\wxDeMPQ.exe" 0
  
SectionEnd

Section
	MessageBox MB_OK "If the application can NOT run on your system, please downlad and install 'vcredist_x86.exe' from download.microsoft.com firstly."
SectionEnd

;--------------------------------

; Uninstaller

Section "Uninstall"
  
  ; Remove registry keys
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\wxDeMPQ"
  DeleteRegKey HKLM SOFTWARE\Jie\wxDeMPQ

  ; Remove files and uninstaller
  Delete $INSTDIR\wxDeMPQ.exe
  Delete $INSTDIR\BLP2PNGLibraryd.dll
	Delete $INSTDIR\DSoundLibraryd.dll
	Delete $INSTDIR\ModelLibraryd.dll
	Delete $INSTDIR\DBQueryLibraryd.dll
	Delete $INSTDIR\DBCFields.xml
	Delete $INSTDIR\WDBFields.xml
;	Delete $INSTDIR\vcredist_x86.exe
  Delete $INSTDIR\uninstall.exe

	RMDir $INSTDIR\cache
	RMDir $INSTDIR
	
  ; Remove shortcuts, if any
  Delete "$SMPROGRAMS\wxDeMPQ\*.*"
	
  ; Remove directories used
  RMDir "$SMPROGRAMS\wxDeMPQ"
  RMDir "$INSTDIR"

SectionEnd
