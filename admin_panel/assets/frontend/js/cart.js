function item_remove_to_cart(id){
	remove_to_cart(id);
	get_cart();
	getCookie("cart");
}

// ============== cookie functions ==============
function checkCookie(name) {
	var dc = document.cookie;
	var prefix = name + "=";
	var begin = dc.indexOf("; " + prefix);
	if (begin == -1) {
		begin = dc.indexOf(prefix);
		if (begin != 0) return false;
	}
	getCookie("cart");
	return true
} 


// Remove item in to coockie
function remove_to_cart(pid) {
	var rcookie =  getCookie("cart");
	for (var i = 0; i < rcookie.length; ++i) {
		if (rcookie[i].pid == pid) {
			rcookie.splice(i, 1);
			set_cookies(rcookie);
			return true;
		}
	}
	
	return false;
}

//chack item in cart
function chack_item_in_cart(val) {
	if(checkCookie("cart")){
		var rcookie =  getCookie("cart");
		for (var i = 0; i < rcookie.length; ++i) {
			if (rcookie[i].pid == val) {
				return true;
			}
		}
		return false;
	}else{
		return false;
	}
}

//get item in cart
function get_item_in_cart(val) {
	if(checkCookie("cart")){
		var rcookie =  getCookie("cart");
		for (var i = 0; i < rcookie.length; ++i) {
			if (rcookie[i].pid == val) {
				return rcookie[i];
			}
		}
		return null;
	}else{
		return null;
	}
}

// add item in to coockie
function add_to_cart(val, with_psd = 0, image_size = '') {
	alert(checkCookie("cart"));
	if(checkCookie("cart")){
		var rcookie =  getCookie("cart");
		for (var i = 0; i < rcookie.length; ++i) {
			if (rcookie[i].pid == val) {
				
				rcookie[i].with_psd = with_psd;
				rcookie[i].image_size = image_size;
				set_cookies(rcookie);
				return true;
			}
		}
		
		//set new item in to cookie
		var tem_arr = {'pid':val, 'with_psd':with_psd, 'image_size':image_size};
		rcookie.push(tem_arr); 
		set_cookies(rcookie);
	}else{
		//set new item in to cookie
		var tem_arr = [{'pid':val, 'with_psd':with_psd, 'image_size':image_size }];
		set_cookies(tem_arr);
	}
	getCookie("cart");
}

function set_cookies(val){
	var now = new Date();
	var time = now.getTime();
	var expireTime = time + 1000*36000;
	now.setTime(expireTime);
		
	var json_str = JSON.stringify(val);
	document.cookie= 'cart='  + json_str + ';expires='+now.toUTCString()+';path=/';
}

function getCookie(cookieName) {
	var cart_count = 0;
	let cookie = {};
	document.cookie.split(';').forEach(function(el) {
	let [key,value] = el.split('=');
	cookie[key.trim()] = value;
	})
	//console.log(cookie[cookieName]);
	var res = [];
	if(cookie[cookieName] != undefined){
		res = JSON.parse(cookie[cookieName]);
		$('#cart_count').html(res.length);
			console.log(res);
		return res;
	}
	return res;
	
	
	
}

getCookie("cart");
	
// ============ cart ajax ================

function get_cart(){
	$.ajax({
		url:url+'/ajax_get_cart',
		//method:"POST", 
		type: 'GET',
		//data:{ "_token": "{{ csrf_token() }}", data:getCookie("cart") }, 
		dataType: 'json',
		beforeSend: function(){
			$('#mycart').html('<h4 class="d-block text-center mt-3">Cart Loading ...</h4> ');
		},
		success:function(response)
		{  
			//console.warn(response.html);
			$('#mycart').html(response.html);
		},
		error: function(e){ 
			//alertify.error("Somthing Wrong");
		alert("Somthing Wrong");
		console.log(e);
		} 
	});
}
get_cart();
			
			