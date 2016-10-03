﻿using UnityEngine;
using System.Collections;
using KKSpeech;
using UnityEngine.Events;

public class KKSpeechRecognizerListener : MonoBehaviour {

	[System.Serializable]
	public class AuthorizationCallback: UnityEvent<AuthorizationStatus> {};

	[System.Serializable]
	public class ResultCallback: UnityEvent<string> {};

	[System.Serializable]
	public class AvailabilityCallback: UnityEvent<bool> {};

	[System.Serializable]
	public class ErrorCallback: UnityEvent<string> {};

	public AuthorizationCallback onAuthorizationStatusFetched = new AuthorizationCallback();
	public ResultCallback onPartialResults = new ResultCallback();
	public ResultCallback onFinalResults = new ResultCallback();
	public AvailabilityCallback onAvailabilityChanged = new AvailabilityCallback();
	public ErrorCallback onErrorDuringRecording = new ErrorCallback();
	public ErrorCallback onErrorOnStartRecording = new ErrorCallback();

	void AvailabilityDidChange(string available) {
		onAvailabilityChanged.Invoke( available.Equals("1"));
	}

	void GotPartialResult(string result) {
		onPartialResults.Invoke(result);
	}

	void GotFinalResult(string result) {
		onFinalResults.Invoke(result);
	}

	void FailedToStartRecording(string reason) {
		onErrorOnStartRecording.Invoke(reason);
	}

	void FailedDuringRecording(string reason) {
		onErrorDuringRecording.Invoke(reason);
	}

	void AuthorizationStatusFetched(string status) {

		AuthorizationStatus authStatus = AuthorizationStatus.NotDetermined;
		switch (status) {
		case "denied":
			authStatus = AuthorizationStatus.Denied;
			break;
		case "authorized":
			authStatus = AuthorizationStatus.Authorized;
			break;
		case "restricted":
			authStatus = AuthorizationStatus.Restricted;
			break;
		case "notDetermined":
			authStatus = AuthorizationStatus.NotDetermined;
			break;
		}

		onAuthorizationStatusFetched.Invoke(authStatus);
	}
}