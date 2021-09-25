window.onload = function () {
			$.ajax ({
				url: "Profile",
				type: "GET",
				success: function(data)
				{
					var nameUpdate = data.username;
					document.getElementById("nickname").innerHTML = nameUpdate;
					
					var streakUpdate = data.num_streak;
					document.getElementById("mystreak").innerHTML = streakUpdate;

					var sessionUpdate = data.total_sessions;
					document.getElementById("mysession").innerHTML = sessionUpdate;

				},
			});
		}