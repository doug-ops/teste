function showSweetalertWarning(titleMessage, textMessage, timerMessage)
{
	swal({
		  title: titleMessage,
		  text: textMessage,
		  type: "warning",
		  timer: timerMessage,
		  showConfirmButton: true
		});	
	
	if('Acesso Expirado.'==textMessage){
		setTimeout( function() {
			document.getElementById('logoutLink').click();
		}, 1000 );
	}
}

function showSweetalertSuccess(titleMessage, textMessage, timerMessage)
{
	swal({
		  title: titleMessage,
		  text: textMessage,
		  type: "success",
		  timer: timerMessage,
		  showConfirmButton: true
		});	
}

function showLoading(msg)
{
	$('span#msgLoading').text(msg);
	$('#block1').show();
	$('#block2').show();
}

function hideLoading()
{
	$('#block1').hide();
	$('#block2').hide();
}

Number.prototype.format = function(n, x, s, c) {
    var re = '\\d(?=(\\d{' + (x || 3) + '})+' + (n > 0 ? '\\D' : '$') + ')',
        num = this.toFixed(Math.max(0, ~~n));

    return (c ? num.replace('.', c) : num).replace(new RegExp(re, 'g'), '$&' + (s || ','));
}

jQuery.fn.ForceNumericOnly =
	function()
	{
	    return this.each(function()
	    {
	        $(this).keydown(function(e)
	        {
	            var key = e.charCode || e.keyCode || 0;
	            // allow backspace, tab, delete, enter, arrows, numbers and keypad numbers ONLY
	            // home, end, period, and numpad decimal
	            return (
	                key == 8 || 
	                key == 9 ||
	                key == 13 ||
	                key == 46 ||
	                (key >= 35 && key <= 40) ||
	                (key >= 48 && key <= 57) ||
	                (key >= 96 && key <= 105));
	        });
	    });
	};
	
	function showToastDanger(msg)
	{
		$.toast({
            heading: 'Manager Alerta',
            text: msg,
            position: 'top-center',
            loaderBg:'#ff6849',
            icon: 'error',
            hideAfter: 3500
            
        });
	}
	
	function showToastSuccess(msg)
	{
		$.toast({
            heading: 'Manager Alerta',
            text: msg,
            position: 'top-center',
            loaderBg:'#ff6849',
            icon: 'success',
            hideAfter: 3500
        });
	}
	
	function showToastInfo(msg)
	{
		$.toast({
            heading: 'Manager Alerta',
            text: msg,
            position: 'top-center',
            loaderBg:'#ff6849',
            icon: 'info',
            hideAfter: 3500
        });
	}
	
	
	function showToastWarning(msg)
	{
		$.toast({
            heading: 'Manager Alerta',
            text: msg,
            position: 'top-center',
            loaderBg:'#ff6849',
            icon: 'warning',
            hideAfter: 3500
        });
	}
	
    function goToScrollTop(id)
    {
		var $doc = $('html, body');
		$doc.animate({
	        scrollTop: $(id).offset().top
	    }, 300);
    }