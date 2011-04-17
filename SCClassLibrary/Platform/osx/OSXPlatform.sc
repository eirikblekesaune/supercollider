OSXPlatform : UnixPlatform
{
	var <>preferencesAction;
	var <>sleepAction, <>wakeAction, <>isSleeping=false;

	initPlatform {
		super.initPlatform;
		recordingsDir = "~/Music/SuperCollider Recordings".standardizePath;
		this.declareFeature(\unixPipes); // pipes are possible (can't declare in UnixPlatform since IPhonePlatform is unixy yet can't support pipes)
		if (Platform.ideName == "scapp") { this.setDeferredTaskInterval(1/60); }
	}

	name { ^\osx }

	startupFiles {
		var filename = "startup.rtf";
		^[this.systemAppSupportDir +/+ filename, this.userAppSupportDir +/+ filename];
	}

	startup {
		if(Platform.ideName == "scapp"){
			Document.implementationClass.startup;
			// make server window
			Server.internal.makeWindow;
			Server.local.makeWindow;
		};
		this.loadStartupFiles;
	}
	shutdown {
		HIDDeviceService.releaseDeviceList;
		if(Platform.ideName == "scapp"){
			CocoaMenuItem.clearCustomItems;
		};
	}

	// Prefer qt but fall back to cocoa if qt not installed.
	defaultGUIScheme { if (GUI.get(\qt).notNil) {^\qt} {^\cocoa} }
	defaultHIDScheme { ^\osx_hid }

	recompile { _Recompile }

	setDeferredTaskInterval { |interval| _SetDeferredTaskInterval }

	findHelpFile { | string |
		^string.findHelpFile;
	}

	getMouseCoords {
		^this.prGetMouseCoords(Point.new);
	}

	prGetMouseCoords {|point|
		_Mouse_getCoords
		^this.primitiveFailed
	}
}
