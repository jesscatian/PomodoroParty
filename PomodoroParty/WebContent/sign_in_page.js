$('form').on("submit", function() {
	/*if ( $('#username-input').val().trim().length == 0 ) {
		$('#username-input').addClass('is-invalid');
	} 
	else {
		$('#username-input').removeClass('is-invalid');
		// $('#username-input').addClass('is-valid');
	}

	if ( $('#password-input').val().trim().length == 0 ) {
		$('#password-input').addClass('is-invalid');
	} 
	else {
		$('#password-input').removeClass('is-invalid');
		// $('#password-input').addClass('is-valid');
	}*/
	
	console.log("Received a response!");
	$.ajax({ 
	    type: 'POST', 
	    dataType: 'json',
	    url: 'Login', 
	    data: { 
			username: $("#username-input").value,
			password: $("#password-input").value,
		}, 
	    success: function (response) { 
	    	console.log(response.success);
	    	console.log(response)
	    	
	        console.log("Received a response!");
			if(response.success == false){
				alert("Invalid Username/Password");
			}
			else{
			}
		}
	});
	
	
	return ( !document.querySelectorAll('.is-invalid').length > 0 );
});

$("#back-btn").on("click", function(){
    window.location.replace("landing_page.html")
});