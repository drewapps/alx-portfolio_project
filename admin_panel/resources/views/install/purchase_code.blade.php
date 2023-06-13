<!doctype html>
<html lang="en">
  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="{{ asset('assets/installer/css/bootstrap.min.css') }}">
    <link rel="stylesheet" href="{{ asset('assets/installer/css/font-awesome.css') }}">
    <link rel="icon" href="{{asset('assets/images/Brandpeak_7.jpg')}}">
    <link rel="stylesheet" href="{{asset('assets/css/dist/adminlte.min.css')}}">

    <meta name="csrf-token" content="{{ csrf_token() }}">
    <title>Installation - Magik Writer</title>
    <style>
        .master {
            background-image: url('assets/images/web_login_bg.png');
            background-size: cover;
            background-position: top;
            min-height: 100vh;
            display: -webkit-flex;
            display: -ms-flexbox;
            display: flex;
            -webkit-justify-content: center;
            -ms-flex-pack: center;
            justify-content: center;
            -webkit-align-items: center;
            -ms-flex-align: center;
            align-items: center;
        }
        .box {
            border-radius: 0 0 3px 3px;
            overflow: hidden;
            box-sizing: border-box;
            box-shadow: 0 10px 10px rgba(0, 0, 0, .19), 0 6px 3px rgba(0, 0, 0, .23);
        }
    </style>
  </head>
  <body>
    <div class="master">
        <div class="card card-primary" style="width: 30% !important;">
            <div class="card-header">
                <i class="fa fa-key"></i> <span style="font-size:20px;">Your Licence</span>
            </div>
            <div class="card-body">
                <div id="message"></div>
                
                <form id="purchase_form">
                    <div class="form-group">
                        <label for="username">Envato Username: <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" name="username" id="username" placeholder="Enter Envato Username" autocomplete="off" required>
                    </div>

                    <div class="form-group">
                        <label for="purchase_code">Purchase Code: <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" name="purchase_code" id="purchase_code" placeholder="Enter Purchase Code" autocomplete="off" required>
                    </div>

                    <button type="submit" class="btn btn-secondary mb-4">Validate</button>
                </form>

                <form id="code" method="get" action="{{url('database-setup')}}">
                    {!! csrf_field() !!}
                    <input type="hidden" name="purchase_code" id="code_value" value="">
                </form>

                <b>Quick Links :</b>
                <ul style="margin-left:-20px;">
                    <li><a target="_blank" href="https://codecanyon.net/licenses/standard">What The Licence Mean ?</a></li>
                    <li><a href="https://help.market.envato.com/hc/en-us/articles/202822600-Where-Is-My-Purchase-Code-" target="_blank">Where Is My Purchase Code ?</a></li>
                    <li><a target="_blank" href="https://codecanyon.net/user/iqueen">Where I Can Bought a Licence ?</a></li>
                </ul> 
            </div>
        </div>
    </div>
    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src="{{ asset('assets/installer/js/jquery-3.2.1.slim.min.js') }}"></script>
    <script src="{{ asset('assets/installer/js/popper.min.js') }}"></script>
    <script src="{{ asset('assets/installer/js/bootstrap.min.js') }}"></script>
    <script src="{{ asset('assets/installer/js/font-awesome.js') }}"></script>
    <script src="{{asset('assets/js/adminlte.js')}}"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.1/jquery.min.js"></script>
    <script>
    $(document).ready(function(){
        $("#purchase_form").submit(function(e) {
            e.preventDefault();
            var code = $("#purchase_code").val();
            var name = $("#username").val();

            $.ajax({
                url: "{{ url('licence-validation') }}",
                type: "POST",
                data: {purchase_code:code,userName:name},
                headers: { 'X-CSRF-TOKEN': $('meta[name="csrf-token"]').attr('content') },
                success: function(data) {
                    $("#message").empty();
                    if(data.status == "fail")
                    {
                        $('#message').append('<div class="alert alert-danger"><span>'+data.error+'</span></div>');
                    }
                    if(data.status == "success")
                    {
                        $("#code_value").val(data.purchase_code);

                        var domain = data.url;
                        var u_name = data.userName;
                        var p_code = data.purchase_code;
                        var version = "2";

                        $.ajax({
                            url: "https://viplan.in/api/magik-writer-licence-store",
                            type: "GET",
                            data: {username:u_name,url:domain,licence_code:p_code,version:version},
                            dataType: "json",
                            success: function(licence_data) {
                                if(licence_data.status == "success")
                                {
                                    $('#message').append('<div class="alert alert-success"><span>'+data.msg+'</span></div>');
                                    window.setTimeout(function() {
                                        $("#code").submit();
                                    }, 3000);
                                }
                                if(licence_data.status == "fail")
                                {
                                    $('#message').append('<div class="alert alert-danger"><span>'+licence_data.error+'</span><br><span>'+licence_data.domain+'</span></div>');
                                }
                            }
                        });
                    }
                }
            });
        });
    });
    </script>
  </body>
</html>