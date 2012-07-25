function onSUB(){
	var resultArea = $('#result').val();
	var url = '/Demo/ChatServlet?word='+resultArea;
	$('#comet-frame')[0].src = url;
}


function update(data) {
	alert(data);
	$('#result').val(data);
	//resultArea.value = data;
}
