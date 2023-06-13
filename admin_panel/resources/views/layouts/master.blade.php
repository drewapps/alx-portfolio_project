<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="csrf-token" content="{{ csrf_token() }}">
  <title>Admin Dashboard - {{App\Models\Setting::getSetting('app_name')}}</title>
  <link href="{{asset('uploads/'.App\Models\Setting::getSetting('favicon'))}}" rel="icon">
  <!-- Tell the browser to be responsive to screen width -->
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="{{asset('assets/plugins/font-awesome/css/font-awesome.min.css') }}">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/fontawesome.min.css">
  <!-- Ionicons -->
  <link rel="stylesheet" href="{{asset('assets/css/ionicons.min.css')}}">
  <!-- fullCalendar 2.2.5-->
  <link rel="stylesheet" href="{{asset('assets/plugins/fullcalendar/fullcalendar.min.css')}}">
  <link rel="stylesheet" href="{{asset('assets/plugins/fullcalendar/fullcalendar.print.css')}}" media="print">
  <!-- DataTables -->
  <link rel="stylesheet" href="{{asset('assets/plugins/datatables/dataTables.bootstrap4.min.css')}}">
  <link rel="stylesheet" type="text/css" href="{{ asset('assets/css/cdn/buttons.dataTables.min.css')}}">
    <!-- Select2 -->
  <link rel="stylesheet" href="{{asset('assets/plugins/select2/select2.min.css')}}">
  <!-- Theme style -->
  <link rel="stylesheet" href="{{asset('assets/css/dist/adminlte.min.css')}}">
  <!-- iCheck -->
  <link rel="stylesheet" href="{{asset('assets/plugins/iCheck/flat/blue.css')}}">

  <link rel="stylesheet" href="{{ asset('assets/css/openai.css')}}">
  <link rel="stylesheet" href="{{ asset('assets/css/clean-switch.css')}}">
    <!-- iCheck for checkboxes and radio inputs -->
  <link rel="stylesheet" href="{{asset('assets/plugins/iCheck/all.css')}}">
  <!-- Morris chart -->
  <link rel="stylesheet" href="{{asset('assets/plugins/morris/morris.css')}}">
  <!-- jvectormap -->
  <link rel="stylesheet" href="{{asset('assets/plugins/jvectormap/jquery-jvectormap-1.2.2.css')}}">
  <!-- bootstrap wysihtml5 - text editor -->
  <link rel="stylesheet" href="{{asset('assets/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css')}}">
  <!-- Google Font: Source Sans Pro -->
  <link href="{{ asset('assets/css/fonts/fonts.css') }}" rel="stylesheet">
  <link rel="stylesheet" href="//code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-colorpicker/2.5.3/css/bootstrap-colorpicker.min.css" rel="stylesheet">
  <link href="{{ asset('assets/css/pnotify.custom.min.css')}}" media="all" rel="stylesheet" type="text/css" />
  <link href="https://fonts.googleapis.com/css2?family=Poppins&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"/>
  <!--toaster--alert -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.css"/>
  <!-- summernote -->
  <link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.css" rel="stylesheet">
  @yield("extra_css")
  
  <!-- browser notification -->
  <!-- <script type="text/javascript" src="{{asset('assets/push_notification/app.js')}}"></script> -->
  <style type="text/css">
    tfoot input {
        width: 100%;
        padding: 3px;
        box-sizing: border-box;
        font-size: 0.6em;
        height: 35px !important;
    }
    body
    {
        font-family: 'Poppins', sans-serif;
    }
    .dropdown:hover .dropdown-menu 
    {
      display: block;
    }
    .main-sidebar {
      min-height: 90% !important;
    }
    .content-wrapper {
      min-height: 910px !important;
    }
    .card-title
    {
      font-size: 1.4rem;
    }
    .mega-white-logo
    {
      display: inline-block;
      max-height: 45px;
      vertical-align: middle;
    }
    .brand-link {
      padding: 1.1125rem 1.5rem;
      font-size: 1.70rem;
    }
    .layout-navbar-fixed .wrapper .brand-link {
        width: 300px;
    }
    .layout-navbar-fixed .wrapper .main-sidebar:hover .brand-link {
      width: 300px;
    }
    .main-header{
        border-bottom : 0px solid #dee2e6;
    }
    .nav-pills .nav-link:not(.active):hover {
        color: rgb(30 41 59/1);
    }
    .sidebar-mini .main-sidebar .nav-link, .sidebar-mini-md .main-sidebar .nav-link, .sidebar-mini-xs .main-sidebar .nav-link {
        width: auto;
    }
    .layout-fixed .brand-link {
      width: 301px;
    }
    .items-center .active{
        background-color:#B9F3E4;
        color: black !important;
    }
    [multiple], [type=date], [type=datetime-local], [type=email], [type=month], [type=number], [type=password], [type=search], [type=tel], [type=text], [type=time], [type=url], [type=week], select, textarea {
      border-color: #ced4da;
    }
    [multiple]:focus, [type=date]:focus, [type=datetime-local]:focus, [type=email]:focus, [type=month]:focus, [type=number]:focus, [type=password]:focus, [type=search]:focus, [type=tel]:focus, [type=text]:focus, [type=time]:focus, [type=url]:focus, [type=week]:focus, select:focus, textarea:focus {
      border-color: #ced4da;
      box-shadow: none;
    }
    .sidebar-collapse .main-sidebar, .sidebar-collapse .main-sidebar::before {
        margin-left: -301px;
    }
    @media (min-width: 1200px){
      .pushmenu{
        display: none;
      }
    }
    @media (max-width: 1200px){
      .main-sidebar, .main-sidebar::before {
          margin-left: -302px;
      }
    }
    @media (min-width: 768px){
      body:not(.sidebar-mini-md):not(.sidebar-mini-xs):not(.layout-top-nav) .content-wrapper, body:not(.sidebar-mini-md):not(.sidebar-mini-xs):not(.layout-top-nav) .main-footer, body:not(.sidebar-mini-md):not(.sidebar-mini-xs):not(.layout-top-nav) .main-header {
          transition: margin-left .3s ease-in-out;
          margin-left: 302px;
      }
    }
    @media (max-width: 1200px){
      body:not(.sidebar-mini-md):not(.sidebar-mini-xs):not(.layout-top-nav) .content-wrapper, body:not(.sidebar-mini-md):not(.sidebar-mini-xs):not(.layout-top-nav) .main-footer, body:not(.sidebar-mini-md):not(.sidebar-mini-xs):not(.layout-top-nav) .main-header {
          margin-left: 0;
      }
    }
    @media (min-width: 992px){
      .sidebar-mini.sidebar-collapse.layout-fixed .brand-link {
          width: 19rem;
      }
    }
    @media (max-width: 1200px){
      .sidebar-open #sidebar-overlay {
          display: block;
      }
    }
    a.text-light:hover {
      color: #171718!important;
    }
    .nav-pills .nav-link.active
    {
      color:white !important;
    }
  </style>
</head>

<body class="layout-fixed sidebar-mini">
<div class="wrapper">
  <!-- Navbar -->
  <nav class="main-header navbar navbar-expand" style="z-index: 1;">
    <ul class="navbar-nav">
      <li class="nav-item">
        <a class="nav-link pushmenu" data-widget="pushmenu" href="#"><i class="fa fa-bars"></i></a>
      </li>
      <li class="nav-item">
        <h3 class="text-2xl font-bold leading-7 sm:truncate ml-4">@yield('title')</h3>
      </li>
    </ul>

    <ul class="navbar-nav ml-auto">
      <li class="dropdown mt-1 mr-3">
          <a data-toggle="dropdown" href="#">
              <img class="rounded-circle" src="@if(Auth::user()->image) {{ asset('uploads/'.Auth::user()->image) }} @else {{ asset('assets/images/no-user.jpg') }} @endif" width="40px" height="40px">
              <span class="badge badge-danger navbar-badge"></span>
          </a>
          <div class="dropdown-menu dropdown-menu-right">
              @if(Auth::user()->user_type == 'Super Admin' || Auth::user()->user_type == 'Demo')<a class="dropdown-item" href="{{url('admin/update-profile')}}">Profile</a>@endif
              <a class="dropdown-item" href="{{ route('logout') }}" onclick="event.preventDefault(); document.getElementById('logout-form').submit();">Logout</a>
          </div>
          <form id="logout-form" action="{{ route('logout') }}" method="POST" style="display: none;">
              {{ csrf_field() }}
          </form>
      </li>
    </ul>
  </nav>
  <!-- /.navbar -->

  <!-- Main Sidebar Container -->
  <aside class="main-sidebar bg-white" style="width:302px !important;border-right-width: 1px;">
    <!-- Brand Logo -->
    <a href="{{ url('admin/')}}" class="brand-link text-center" style="background-color: #2c2c2c;">
      <img src="{{asset('uploads/'.App\Models\Setting::getSetting('white_logo'))}}" height="40px;">
    </a>
    
    <!-- Sidebar -->
    <div class="sidebar" style="padding-bottom:40px;background-color: #2c2c2c;">
      <!-- Sidebar Menu -->
     <nav class="mt-2">
        <ul class="nav nav-pills nav-sidebar flex-column text-black" data-widget="treeview" role="menu" data-accordion="false">
            <li class="flex items-center">
                <a href="{{ url('admin/')}}" class="text-light w-full rounded-md hover:bg-gray-50 group flex items-center justify-between p-3 text-gray-600 hover:text-gray-800 font-medium @if(Request::is('admin') || Request::is('admin/user-profile') || Request::is('admin/package-detail*')) active @endif">
                    <span class="flex items-center">
                        <i class="nav-icon fas fa-tachometer-alt mr-2"></i>
                        Dashboard
                        <span class="right badge badge-danger"></span>
                    </span>
                </a>
            </li>

            <li class="flex items-center">
                <a class="text-light w-full rounded-md hover:bg-gray-50 group flex items-center justify-between p-3 text-gray-600 hover:text-gray-800 font-medium @if(Request::is('admin/user*') && !Request::is('admin/user-profile') && !Request::is('admin/user-delete-request*')) active @endif" href="{{route('user.index')}}">
                    <span class="flex items-center">
                        <i class="nav-icon fa fa-users mr-2"></i>
                        User
                    </span>
                </a>
            </li>

            <li class="flex items-center">
                <a class="text-light w-full rounded-md hover:bg-gray-50 group flex items-center justify-between p-3 text-gray-600 hover:text-gray-800 font-medium @if(Request::is('admin/uses*') || Request::is('admin/today-generated-document') || Request::is('admin/today-generated-image')) active @endif" href="{{url('admin/uses')}}">
                    <span class="flex items-center">
                        <i class="nav-icon fa fa-recycle mr-2"></i>
                        Report
                    </span>
                </a>
            </li>
          
            <li class="flex items-center">
                <a class="text-light w-full rounded-md hover:bg-gray-50 group flex items-center justify-between p-3 text-gray-600 hover:text-gray-800 font-medium @if(Request::is('admin/plan*')) active @endif" href="{{route('plan.index')}}">
                    <span class="flex items-center">
                        <i class="nav-icon fa fa-dollar-sign mr-2"></i>
                        Subscriptions
                    </span>
                </a>
            </li>
            <!-- <li class="flex items-center">
                <a class="w-full rounded-md hover:bg-gray-50 group flex items-center justify-between p-3 text-gray-600 hover:text-gray-800 font-medium @if(Request::is('admin/coupon-code*')) active @endif" href="{{route('coupon-code.index')}}">
                    <span class="flex items-center">
                        <i class="nav-icon fa-solid fa-ticket mr-2"></i>
                        Coupon Code
                    </span>
                </a>
            </li> -->
            <li class="flex items-center">
                <a class="text-light w-full rounded-md hover:bg-gray-50 group flex items-center justify-between p-3 text-gray-600 hover:text-gray-800 font-medium @if(Request::is('admin/transaction*')) active @endif" href="{{ url('admin/transaction')}}">
                    <span class="flex items-center">
                        <i class="nav-icon fa fa-list mr-2"></i>
                        Transactions
                    </span>
                </a>
            </li>
            <li class="flex items-center">
                <a class="text-light w-full rounded-md hover:bg-gray-50 group flex items-center justify-between p-3 text-gray-600 hover:text-gray-800 font-medium @if(Request::is('admin/template*')) active @endif" href="{{ url('admin/template')}}">
                    <span class="flex items-center">
                        <i class="nav-icon fa fa-scroll mr-2"></i>
                        Templates
                    </span>
                </a>
            </li>
            <li class="flex items-center">
                <a class="text-light w-full rounded-md hover:bg-gray-50 group flex items-center justify-between p-3 text-gray-600 hover:text-gray-800 font-medium @if(Request::is('admin/support-requests*')) active @endif" href="{{url('admin/support-requests')}}">
                    <span class="flex items-center">
                        <i class="fa-regular fa-message mr-2"></i>
                        Support Requests
                    </span>
                </a>
            </li>
            <li class="flex items-center">
                <a class="text-light w-full rounded-md hover:bg-gray-50 group flex items-center justify-between p-3 text-gray-600 hover:text-gray-800 font-medium @if(Request::is('admin/user-delete-request*')) active @endif" href="{{ url('admin/user-delete-request')}}">
                    <span class="flex items-center">
                        <i class="nav-icon fa fa-user-xmark mr-2"></i>
                        Delete Request
                    </span>
                </a>
            </li>
            <li class="flex items-center">
                <a class="text-light w-full rounded-md hover:bg-gray-50 group flex items-center justify-between p-3 text-gray-600 hover:text-gray-800 font-medium @if(Request::is('admin/inquiry*')) active @endif" href="{{ url('admin/inquiry')}}">
                    <span class="flex items-center">
                        <i class="nav-icon fa fa-envelope mr-2"></i>
                        Contact List
                    </span>
                </a>
            </li>

            <!-- @if(Request::is('admin/blog*'))
              @php($class="menu-open")
              @php($active="active")
            @else
              @php($class="")
              @php($active="")
            @endif
            <li class="nav-item items-center has-treeview {{$class}}">
              <a href="#" class="text-light nav-link w-full rounded-md hover:bg-gray-50 group flex items-center justify-between p-3 text-gray-600 hover:text-gray-800 font-medium {{$active}}">
                <span class="flex items-center">
                  <i class="nav-icon fa fa-cube mr-1" style="margin-left:-0.3rem;font-size:1rem;"></i>
                  Blog
                </span>
                <i class="right fa fa-angle-left" style="top:1.2rem;"></i>
              </a>
              <ul class="nav nav-treeview">
                <li class="flex items-center ml-3">
                  <a class="text-light w-full rounded-md hover:bg-gray-50 group flex items-center justify-between p-3 text-gray-600 hover:text-gray-800 font-medium @if(Request::is('admin/blog-category*')) active @endif" href="{{route('blog-category.index')}}">
                      <span class="flex items-center">
                          <i class="nav-icon fa fa-sitemap mr-2"></i>
                          Category
                      </span>
                  </a>
                </li>
                <li class="flex items-center ml-3">
                    <a class="text-light w-full rounded-md hover:bg-gray-50 group flex items-center justify-between p-3 text-gray-600 hover:text-gray-800 font-medium @if(Request::is('admin/blog*') && !Request::is('admin/blog-category*')) active @endif" href="{{route('blog.index')}}">
                      <span class="flex items-center">
                          <i class="nav-icon fa fa-square-rss mr-2"></i>
                          Blog
                      </span>
                    </a>
                </li>
              </ul>
            </li> -->

            <li class="flex items-center">
                <a class="text-light w-full rounded-md hover:bg-gray-50 group flex items-center justify-between p-3 text-gray-600 hover:text-gray-800 font-medium @if(Request::is('admin/setting*')) active @endif" href="{{ url('admin/setting')}}">
                    <span class="flex items-center">
                        <i class="nav-icon fa fa-gear mr-2"></i>
                        Settings
                    </span>
                </a>
            </li>
        </ul>
      </nav>
      <!-- /.sidebar-menu -->
    </div>
    <!-- /.sidebar -->
  </aside>

  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper bg-white">
    <!-- Content Header (Page header) -->
    <!-- <div class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h4 class="m-0 text-dark">@yield('heading') </h4>
          </div>
          <div class="col-sm-6">
          </div>
        </div>
      </div>
    </div> -->

    <!-- /.content-header -->

    <!-- Main content -->
    <section class="content" style="padding-right:20px;">
        @yield('content')
    </section>
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->
  <!-- <footer class="main-footer" style="background: url({{ asset('assets/images/cultrl-bottom-bg2.png') }}) no-repeat;background-size: cover; background-position: bottom; min-height: 333px; width: auto;">
    <div class="row d-flex justify-content-between text-light" style="margin-top: 270px;color:black;">
      <div class="float-left d-sm-inline-block text-dark">
        <strong>© 2022 Power by Festival Pack</strong>
      </div>
      <div class="float-right d-none d-sm-inline-block text-dark">
        <b>All Rights Reserved.</b>
      </div>
    </div>
  </footer> -->

  <!-- Control Sidebar -->
  <aside class="control-sidebar control-sidebar-dark">
    <!-- Control sidebar content goes here -->
  </aside>
  <!-- /.control-sidebar -->
</div>
<!-- ./wrapper -->
@yield('script2')
<!-- jQuery -->
<script src="{{asset('assets/plugins/jquery/jquery.min.js')}}"></script>
<!-- jQuery UI 1.11.4 -->
<script src="{{asset('assets/js/jquery-ui.min.js')}}"></script>
<!-- Resolve conflict in jQuery UI tooltip with Bootstrap tooltip -->
<script>
  $.widget.bridge('uibutton', $.ui.button)
</script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/js/fontawesome.min.js"></script>
<!-- Bootstrap 4 -->
<script src="{{asset('assets/plugins/bootstrap/js/bootstrap.bundle.min.js')}}"></script>
<!-- Select2 -->
<script src="{{asset('assets/plugins/select2/select2.full.min.js')}}"></script>
<!-- iCheck 1.0.1 -->
<script src="{{asset('assets/plugins/iCheck/icheck.min.js')}}"></script>
<!-- FastClick -->
<script src="{{asset('assets/plugins/fastclick/fastclick.js')}}"></script>
<!-- DataTables -->
<script src="{{asset('assets/js/cdn/jquery.dataTables.min.js')}}"></script>
<script src="https://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-colorpicker/2.5.3/js/bootstrap-colorpicker.min.js"></script>
<script src="{{asset('assets/plugins/datatables/dataTables.bootstrap4.min.js')}}"></script>
<script type="text/javascript" src="{{ asset('assets/js/cdn/dataTables.buttons.min.js')}}"></script>
<script type="text/javascript" src="{{ asset('assets/js/cdn/buttons.print.min.js')}}"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"></script>
<!-- AdminLTE App -->
<script src="{{asset('assets/js/adminlte.js')}}"></script>
<script src="{{asset('assets/js/fontawesome.js')}}"></script>
<!-- summernote -->
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
  $('#data_table tfoot th').each( function () {
    // console.log($('#data_table tfoot th').length);
    if($(this).index() != $('#data_table tfoot th').length - 1) {
      var title = $(this).text();
      $(this).html( '<input type="text" placeholder="'+title+'" />' );
    }
  });

  //alert($('.content').height());
  //alert($('.content-wrapper').height());
  
  var table = $('#data_table').DataTable({
      "order": [[ 0, 'desc' ]],
     columnDefs: [ { orderable: false, targets: [0] } ],
     // individual column search
     "initComplete": function() {
            table.columns().every( function () {
                var that = this;
                $('input', this.footer()).on('keyup change', function () {
                  // console.log($(this).parent().index());
                    that.search(this.value).draw();
                });
              });
            }
  });

  $('[data-toggle="tooltip"]').tooltip();

  $('.ToastrButton').click(function() {
    toastr.error('This Action Not Available Demo User');
  });
});
</script>
<script type="text/javascript" src="{{asset('assets/js/pnotify.custom.min.js')}}"></script>
<!-- AdminLTE for demo purposes -->
<!-- <script src="{{ asset('assets/js/demo.js') }}"></script> -->
@yield('script')
</body>
</html>