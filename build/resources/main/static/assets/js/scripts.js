function validate() {
    var isValid = true;

    var titleValue = document.forms["listingForm"]["title"].value;
    if (titleValue.length < 3 || titleValue.length > 100 || titleValue.charAt(0) === " "){
        document.getElementById("titleError").style.visibility = "visible";
        isValid = false;
        }

    var descriptionValue = document.forms["listingForm"]["description"].value;
    if (descriptionValue.length < 3 || titleValue.length > 5000 || descriptionValue.charAt(0) === " "){
        document.getElementById("descriptionError").style.visibility = "visible";
        isValid = false;
        }

    var files = [];
    var j = 5;
    while (j <= 9) {
        files.push(document.getElementsByTagName("input")[j])
        j ++;
    }
    for (var i = 0; i < files.length; i++) {
        var extension = files[i].value.toLowerCase().substring(files[i].lastIndexOf('.') + 1);
        if (!(extension === "gif" || extension === "png" || extension === "jpg" || extension === "jpeg" || files[i]
            === "")) {
            document.getElementById("photoErrorType").style.visibility = "visible";
            isValid = false;
        }
        if (files[i].size > 1000000) {
            document.getElementById("photoErrorSize").style.visibility = "visible";
            isValid = false;
        }
    }
    return isValid;
}

function modalFunction(img) {
    var modal = $(img).siblings("div")[0];
    var modalImg = $(modal).find("img")[0];
    modal.style.display = "block";
    modalImg.src = img.src;
    var span = $(modal).find("span")[0];

    span.onclick = function() {
        modal.style.display = "none";
    };
}

function hideImage(img) {
    img.style.display = "none";
}

