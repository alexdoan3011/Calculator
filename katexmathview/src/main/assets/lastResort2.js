function myFunction(str, blink) {
    // document.getElementsByTagName('BODY')[0].style.display = "block";
    let str2 = "$" + AMTparseAMtoTeX(str) + "$";
//    str2 = str2.replace("\\mid", "\\color{white}{|}\\!");
    if (blink) {
        str2 = str2.replace("\\mid", "\\color{white}{|}\\!");
        document.getElementsByTagName('BODY')[0].innerHTML = str2;
    } else {
        str2 = str2.replace("\\mid", "\\color{black}{|}\\!");
        document.getElementsByTagName('BODY')[0].innerHTML = str2;
    }
}