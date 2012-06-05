;
; AutoHotkey Version: 1.x
; Language:       English
; Platform:       Win9x/NT
; Author:         A.N.Other <myemail@nowhere.com>
;
; Script Function:
;	Template script (you can customize this template by editing "ShellNew\Template.ahk" in your Windows folder)
;

#NoEnv  ; Recommended for performance and compatibility with future AutoHotkey releases.
SendMode Input  ; Recommended for new scripts due to its superior speed and reliability.
SetWorkingDir %A_ScriptDir%  ; Ensures a consistent starting directory.

F4::
	IfWinExist Brood War
	{
		WinClose Brood War
		Sleep 3000
	}
	IfWinExist LIDA Framework
	{
		WinClose LIDA Framework
	}
	Send {Click 180, 60}
	WinWait LIDA Framework
	ifWinExist Chaoslauncher
	{
		WinActivate
		Sleep 200
		Send {Click 60, 360}
		WinWait Brood War
		IfWinExist Brood War
		{
			WinActivate Brood War
			Sleep 2000
			Send {Click 200, 300}
			Send {Click 370, 300}
			Sleep 1000
			Send {Click 90, 205}
			Send {Click 520, 420}
			Sleep 200
			Send {Click 520, 420}
			Sleep 200
			Send {Click 130, 370}
			Sleep 200
			Send {Click 180, 245}
			Send {Click 520, 420}
			Send {Click 420, 90}
			Sleep 500
			SendEvent {Click 140, 90, down}{Click 140, 135, up}
			Sleep 50
			SendEvent {Click 270, 70, down}{Click 270, 122, up}
			Send {Click 520, 420}	
		}
	}
	else
	{
		MsgBox, , , ChaosLauncher window does not exist	
	}
	return
