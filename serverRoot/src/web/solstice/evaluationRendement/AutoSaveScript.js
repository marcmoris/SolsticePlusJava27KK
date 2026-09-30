function getFormData(form) {
    var dataString = "";

    function addParam(name, value) {
        dataString += (dataString.length > 0 ? "&" : "")
            + escape(name).replace(/\+/g, "%2B") + "="
            + escape(value ? value : "").replace(/\+/g, "%2B");
    }

    var elemArray = form.elements;
    for (var i = 0; i < elemArray.length; i++) {
        var element = elemArray[i];
        var elemType = element.type.toUpperCase();
        var elemName = element.name;
        if (elemName) {
            if (elemType == "TEXT"
                    || elemType == "TEXTAREA"
                    || elemType == "PASSWORD"
                    || elemType == "HIDDEN")
                addParam(elemName, element.value);
            else if (elemType == "CHECKBOX" && element.checked)
                addParam(elemName, element.value ? element.value : "On");
            else if (elemType == "RADIO" && element.checked)
                addParam(elemName, element.value);
            else if (elemType.indexOf("SELECT") != -1)
                for (var j = 0; j < element.options.length; j++) {
                    var option = element.options[j];
                    if (option.selected)
                        addParam(elemName,
                            option.value ? option.value : option.text);
                }
        }
    }
    return dataString;
}

var autoSaveDebug = true;

function submitFormData(form) {
    var xhr;
    if (window.ActiveXObject)
        xhr = new ActiveXObject("Microsoft.XMLHTTP");
    else if (window.XMLHttpRequest)
        xhr = new XMLHttpRequest();
    else
        return null;
        
    var method = form.method ? form.method.toUpperCase() : "GET";
    var action = form.action ? form.action : document.URL;
    var data = getFormData(form);

    var url = action;
    if (data && method == "GET")
        url += "?" + data;
    xhr.open(method, url, true);
    
    function submitCallback() {
        if (autoSaveDebug && xhr.readyState == 4 && xhr.status != 200) {
            autoSaveDebug = false;
            alert("Auto-Save Error: "
                + xhr.status + " " + xhr.statusText);
        }
    }
    xhr.onreadystatechange = submitCallback;

    xhr.setRequestHeader("Ajax-Request", "Auto-Save");
    if (method == "POST") {
        xhr.setRequestHeader("Content-Type",
            "application/x-www-form-urlencoded");
        xhr.send(data);
    } else
        xhr.send(null);
    
    return xhr;
}

var autoSaveXHR = new Array(document.forms.length);

function submitAllForms() {
    var formArray = document.forms;
    for (var i = 0; i < formArray.length; i++) {
        if (autoSaveXHR[i]) {
            var oldXHR = autoSaveXHR[i];
            oldXHR.onreadystatechange = function() { };
            oldXHR.abort();
            delete oldXHR;
        }
        autoSaveXHR[i] = submitFormData(formArray[i]);
    }
}

var autoSaveIntervalId = null;

function setAutoSaving(millisec) {
    if (autoSaveIntervalId) {
        clearInterval(autoSaveIntervalId);
        autoSaveIntervalId = null;
    }
    if (millisec != 0)
        autoSaveIntervalId = setInterval(
                "submitAllForms()", millisec);
}

function isAutoSaving() {
    return autoSaveIntervalId != null;
}
