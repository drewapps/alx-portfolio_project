<!doctype html>
<html lang="en">
  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
    <title>Login</title>
    <link href="<?php echo e(asset('uploads/'.App\Models\Setting::getSetting('favicon'))); ?>" rel="icon">
    <style>
        a:hover
        {
            text-decoration: none;
        }
        div.main-section{
            position: relative;
        }
        div.main-section .overlap-text{
            position: absolute;
            top: 50%;
            left:0%;
            transform: translate(0%,-50%);
            width:100%;
        }
    </style>
  </head>
  <body>
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-5 main-section">
                <span class="overlap-text text-light text-center">
                    <span style="font-size:25px;">Nice to see you again</span><br>
                    <span style="font-size:60px;letter-spacing: 3px;"><b>WELCOME BACK</b></span><br>

                    <div class="row mt-3"><div class="col-md-6 offset-md-3">Welcome to our AI writing tools business! Our cutting-edge technology enables you to create high-quality content with ease.</div></div>
                </span>
                <img src="<?php echo e(asset('assets/images/bg_login.png')); ?>" alt="image" style="height:965px;margin-left:-15px;width:104%;">
            </div>
            <div class="col-md-7">
                <div class="container py-5 h-100">
                    <div class="row d-flex justify-content-center align-items-center h-100">
                        <div class="col-12 col-md-8 col-lg-6">
                            <div class="p-4 text-center">
                                <h1 class="text-primary">Login Account</h1>
                                <div class="mt-3 mb-5">Login to your account and manage your business with easy options</div>
                                <?php if(count($errors) > 0): ?>
                                <div class="alert alert-danger">
                                    <ul>
                                        <?php $__currentLoopData = $errors->all(); $__env->addLoop($__currentLoopData); foreach($__currentLoopData as $error): $__env->incrementLoopIndices(); $loop = $__env->getLastLoop(); ?>
                                        <li><?php echo e($error); ?></li>
                                        <?php endforeach; $__env->popLoop(); $loop = $__env->getLastLoop(); ?>
                                    </ul>
                                </div>
                                <?php endif; ?>
                                
                                <form class="form-horizontal" role="form" method="POST" action="<?php echo e(route('login')); ?>">
                                <?php echo e(csrf_field()); ?>

                                <div class="form-outline mb-3">
                                    <input type="email" class="form-control form-control-lg" style="font-size:20px;" placeholder="EMAIL" id="email" name="email" value="<?php echo e(old('email')); ?>" autofocus required>
                                </div>

                                <div class="form-outline">
                                    <input type="password" class="form-control form-control-lg" style="font-size:20px;" placeholder="PASSWORD" id="password" name="password" required>
                                </div>

                                <div class="row">
                                    <div class="col-md-6 form-check mt-3 ml-3 text-left">
                                        <input class="form-check-input" type="checkbox" value="" id="form1Example3" style="width:20px;height:20px;"/>
                                        <label class="form-check-label ml-1" for="form1Example3" style="font-size:20px;"> Keep me signed in </label>
                                    </div>
                                    <div class="col-md-5 form-check mt-3 text-right" style="padding-right:0px;">
                                        <a href="<?php echo e(route('password.request')); ?>" target="_blank" class="text-primary" style="font-size:20px;"><b>Forgot Password?</b></a>
                                    </div>
                                </div>

                                <button class="btn btn-primary btn-lg btn-block mt-5" type="submit" style="font-size:22px;border-radius:25px;">LOGIN</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- <section class="vh-100" style="background-image: url('<?php echo e(asset('assets/images/web_login_bg.png')); ?>');">
        <div class="container py-5 h-100">
            <div class="row d-flex justify-content-center align-items-center h-100">
                <div class="col-12 col-md-8 col-lg-6 col-xl-5">
                    <div class="card shadow-2-strong shadow shadow-lg" style="border-radius: 5px;">
                        <div class="card-body p-4 text-center">
                            <img src="<?php echo e(asset('uploads/'.App\Models\Setting::getSetting('black_logo'))); ?>" width="300px" height="70px">
                            <h3 class="mt-4 mb-1" style="color:#f77b0b">Welcome To <?php echo e(App\Models\Setting::getSetting('app_name')); ?></h3>
                            <div class="mb-3">Login to your account.</div>
                            <?php if(count($errors) > 0): ?>
                            <div class="alert alert-danger">
                                <ul>
                                    <?php $__currentLoopData = $errors->all(); $__env->addLoop($__currentLoopData); foreach($__currentLoopData as $error): $__env->incrementLoopIndices(); $loop = $__env->getLastLoop(); ?>
                                    <li><?php echo e($error); ?></li>
                                    <?php endforeach; $__env->popLoop(); $loop = $__env->getLastLoop(); ?>
                                </ul>
                            </div>
                            <?php endif; ?>
                            
                            <form class="form-horizontal" role="form" method="POST" action="<?php echo e(route('login')); ?>">
                            <?php echo e(csrf_field()); ?>

                            <div class="form-outline mb-3">
                                <input type="email" class="form-control form-control-lg" style="font-size:15px;" placeholder="EMAIL" id="email" name="email" value="<?php echo e(old('email')); ?>" autofocus required>
                            </div>

                            <div class="form-outline">
                                <input type="password" class="form-control form-control-lg" style="font-size:15px;" placeholder="PASSWORD" id="password" name="password" required>
                            </div>

                            <div class="row">
                                <div class="col-sm-5 form-check mt-3 ml-3 text-left">
                                    <input class="form-check-input" type="checkbox" value="" id="form1Example3" />
                                    <label class="form-check-label" for="form1Example3"> Remember me </label>
                                </div>
                                <div class="col-sm-6 form-check mt-3 ml-3 text-right">
                                    <a href="<?php echo e(route('password.request')); ?>" target="_blank" class="text-dark">Forgot Password?</a>
                                </div>
                            </div>

                            <button class="btn btn-primary btn-lg btn-block mt-3" type="submit" style="font-size:17px;background-color:#f77b0b;border-color:#f77b0b">Login</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section> -->
    <!-- Option 1: jQuery and Bootstrap Bundle (includes Popper) -->
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
  </body>
</html><?php /**PATH /home/u982620258/domains/drewapps.com/public_html/aiassistant/resources/views/auth/login.blade.php ENDPATH**/ ?>