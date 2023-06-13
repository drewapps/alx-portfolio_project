<!doctype html>
<html lang="en">
  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="{{ asset('assets/installer/css/bootstrap.min.css') }}">
    <link rel="stylesheet" href="{{ asset('assets/installer/css/font-awesome.css') }}">
    <link rel="icon" href="{{asset('assets/installer/img/Brandpeak_7.jpg')}}">
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
        <div class="box" style="width: 30% !important;">
            <div class="card card-primary">
                <div class="card-header">
                    <i class="fa-solid fa-database"></i> <span style="font-size:20px;">Database Information</span>
                </div>
                <div class="card-body">
                    @if(session('message') != "")
                        <div class="alert alert-danger">
                            <ul>
                                <li>{{session('message')}} </li>
                            </ul>
                        </div>
                    @endif
                    <form method="post" action="{{ url('database-setup-post') }}">
                        {!! csrf_field() !!}
                        <input type="hidden" name="purchase_code" value="{{ $code }}">
                        <div class="form-group">
                            <label for="host">Database host: <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" name="database_host" id="host" placeholder="Database host" autocomplete="off" required>
                        </div>

                        <div class="form-group">
                            <label for="name">Database Name: <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" name="database_name" id="name" placeholder="Database Name" autocomplete="off" required>
                        </div>

                        <div class="form-group">
                            <label for="username">Database Username: <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" name="database_username" id="username" placeholder="Database Username" autocomplete="off" required>
                        </div>

                        <div class="form-group">
                            <label for="password">Database Password: <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" name="database_password" id="password" placeholder="Database Password" autocomplete="off">
                        </div>

                        <button type="submit" class="btn btn-secondary mb-4">Continue</button>
                    </form>
                </div>
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
  </body>
</html>