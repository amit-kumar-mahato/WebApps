function formValidation(){
	var fname = document.registrationForm.firstName;
	if(fname!=null || fname!=""){
		alert("Comining inside if block");
		return true;
	}
	else{
		alert("Name Required");
	}
}
