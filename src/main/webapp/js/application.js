function onSUB(){
	var login = $('#login').val();
	window.location.href="chat.html?login="+login;
}


function onChat(){
	var resultArea = $('#result').val();
	var url = '/Demo/ChatServlet?word='+resultArea;
	$('#comet-frame')[0].src = url;
}


function update(data) {
	$('#result').val(data);
}
