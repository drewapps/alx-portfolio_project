<!doctype html>
<html lang="en">
  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
    <title>Register</title>
    <style>
        a:hover
        {
            text-decoration: none;
        }
    </style>
  </head>
  <body>
    <section class="min-vh-100" style="background-color: #000000;">
        <div class="container">
            <div class="row py-3 d-flex justify-content-center align-items-center min-vh-100">
                <div class="col-12 col-md-8 col-lg-6 col-xl-5">
                    <div class="card shadow-2-strong" style="border-radius: 5px;">
                        <div class="card-body p-5 text-center">
                            <img src="{{asset('uploads/'.App\Models\Setting::getSetting('black_logo'))}}" width="300px" height="70px">
                            <h3>Register</h3>
                            
                            @if (count($errors) > 0)
                            <div class="alert alert-danger">
                                <ul>
                                    @foreach ($errors->all() as $error)
                                    <li>{{ $error }}</li>
                                    @endforeach
                                </ul>
                            </div>
                            @endif
                            
                            <form class="form-detail" role="form" method="POST" id="myform" action="{{ route('register') }}">
                            {{ csrf_field() }}
                                    
                            <div class="form-group">
                                <div class="form-row">
                                    <input type="text" class="form-control form-control-lg" placeholder="Name" style="font-size:15px;" name="name" value="{{ old('name') }}" id="name" autofocus required>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="form-row">
                                    <input type="email" class="form-control form-control-lg" name="email" placeholder="Email" style="font-size:15px;" value="{{ old('email') }}" id="email" autofocus required pattern="[^@]+@[^@]+.[a-zA-Z]{2,6}">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="form-row">
                                    <input type="password" name="password" id="password" placeholder="Password" style="font-size:15px;" class="form-control form-control-lg" required>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="form-row">
                                    <input type="password" name="password_confirmation" id="comfirm_password" style="font-size:15px;" placeholder="Comfirm Password" class="form-control form-control-lg" required>
                                </div>
                            </div>
                            
                            <!-- <div class="form-group">
                                <div class="form-row mt-4">
                                    <div class="captcha">
                                    <span></span>
                                    <button type="button" class="btn btn-danger" class="reload" id="reload">
                                        &#x21bb;
                                    </button>
                                    </div>
                                    <input id="captcha" type="text" class="form-control form-control-lg mt-2" style="font-size:15px;" placeholder="Captcha" name="captcha">
                                </div>
                            </div> -->
                                
                            <div class="form-row-last mt-3">
                                <button class="btn btn-primary btn-lg btn-block" type="submit" style="font-size:17px;">SIGN UP</button>
                            </div>

                            <p class="text-center mt-4">Already Account? <a href="{{ route('login') }}" class="text-primary text-bold">Sign In</a></p>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!-- Option 1: jQuery and Bootstrap Bundle (includes Popper) -->
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
  </body>
</html>