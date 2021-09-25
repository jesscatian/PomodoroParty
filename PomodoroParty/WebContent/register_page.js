$('form').on("submit", function() {
	if ( $('#username-input').val().trim().length == 0 ) {
		$('#username-input').addClass('is-invalid');
	} 
	else {
		$('#username-input').removeClass('is-invalid');
	}

	if ( $('#password-input').val().trim().length == 0 ) {
		$('#password-input').addClass('is-invalid');
	} 
	else {
		$('#password-input').removeClass('is-invalid');
	}

	if ( $('#nickname-input').val().trim().length == 0 ) {
		$('#nickname-input').addClass('is-invalid');
	} 
	else {
		$('#nickname-input').removeClass('is-invalid');
	}

	if ( $('#passwordCheck-input').val().trim().length == 0 ) {
		$('#passwordCheck-input').addClass('is-invalid');
	} 
	else {
		$('#passwordCheck-input').removeClass('is-invalid');
	}

	return ( !document.querySelectorAll('.is-invalid').length > 0 );
});

$("#back-btn").on("click", function(){
    window.location.replace("landing_page.html")
});