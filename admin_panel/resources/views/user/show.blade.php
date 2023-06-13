@extends('layouts.master')

@section('extra_css')
<style type="text/css">
.profile_img{
    height: 100px;
    width: 100px;
    border: 1px solid #eee;
    position: absolute;
    left: 50%;
    top: 0;
    transform: translate(-50%,-50%);
}

.userCard{
    position:relative;
    width: 100%;
    border-radius: 5px;
    border: none;
    
}

.panel-title {
	cursor:pointer;
}
h4.tab-title
{
	font-family: "avenirheavy", Helvetica, Arial, "sans-serif";
	font-weight: normal;
	font-size: 22px;
	color: #ffffff;
}
.vertab-content ul, .vertab-content ol {
	padding-left: 15px;
}
@media (min-width:768px) {
.vertab-container {
	z-index: 10;
	background-color: #fff;
	padding: 0 !important;
	margin-top: 20px;
	background-clip: padding-box;
	opacity: 0.97;
	filter: alpha(opacity=97);
	overflow: auto;
	margin-bottom: 50px;
}
.vertab-menu {
	padding-right: 0;
	padding-left: 0;
	padding-bottom: 0;
	display: block;
	background-color: #ffff;
}
.vertab-menu .list-group {
	margin-bottom: 0;
}
.vertab-menu .list-group>a {
	margin-bottom: 0;
	border-radius: 0;
}
.vertab-menu .list-group>a, .vertab-menu .list-group>a {
	color: #818181;
	background-image: none;
	background-color: #F6F6F6;
	border-radius: 0;
	box-sizing: border-box;
	border: none;
	border-bottom: 1px solid #CACACA;
	padding: 15px 10px;
}
.vertab-menu .list-group>a.active, .vertab-menu .list-group>a:hover, .vertab-menu .list-group>a:focus {
	position: relative;
	border: none;
	border-radius: 0;
	border-bottom: 1px solid #CACACA;
	border-left: 5px solid #7952b3;
	padding-left: 5px;
	background-image: none;
	background-color: #F6F6F6;
	color: #7952b3;
}
.vertab-content {
	padding-left: 20px;
	padding-top: 10px;
	color: #000;
}
.vertab-accordion .vertab-content:not(.active) {
	display: none;
}
.vertab-accordion .vertab-content.active .collapse {
	display: block;
}	
.vertab-container .panel-heading {
	display: none;
}
.vertab-container .panel-body {
	border-top: none !important;
}
}

/* If the tc_breakpoint variable is changed, this breakpoint should be changed as well */
@media (max-width:767px) {
.vertab-container {
	margin-top: 20px;
	margin-bottom: 20px;
}
.vertab-container .vertab-menu {
	display: none;
}
.vertab-container .panel-heading {
	background-color: #F6F6F6;
	color: #818181;
	padding: 15px;
	border-bottom: 1px solid #F6F6F6;
	border-top-left-radius: 0;
	border-top-right-radius: 0;
	border-left: 5px solid #F6F6F6;
}
.vertab-container .panel-heading:hover, .vertab-container .panel-heading:focus, .vertab-container .panel-heading.active {
	border-left: 5px solid #7952b3;
	border-bottom: 1px solid #7952b3;
}
.vertab-content {
	border-bottom: 1px solid #CACACA;
}
.vertab-container .panel-title a:focus, .vertab-container .panel-title a:hover, .vertab-container .panel-title a:active {
	color: #818181;
	text-decoration: none;
}
.panel-collapse.collapse, .panel-collapse.collapsing {
	background-color: #ffffff !important;
	color: #000;
}
.vertab-container .panel-collapse .panel-body {
	border-top: none !important;
}
}

.switch {
  position: relative;
  display: inline-block;
  width: 50px;
  height: 25px;
}

/* Hide default HTML checkbox */
.switch input {display:none;}

/* The slider */
.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  -webkit-transition: .4s;
  transition: .4s;
}

.slider:before {
  position: absolute;
  content: "";
  height: 16px;
  width: 16px;
  left: 4px;
  bottom: 4px;
  background-color: white;
  -webkit-transition: .4s;
  transition: .4s;
}

input:checked + .slider {
  background-color: #2196F3;
}

input:focus + .slider {
  box-shadow: 0 0 1px #2196F3;
}

input:checked + .slider:before {
  -webkit-transform: translateX(26px);
  -ms-transform: translateX(26px);
  transform: translateX(26px);
}

/* Rounded sliders */
.slider.round {
  border-radius: 34px;
}

.slider.round:before {
  border-radius: 50%;
}

.select2-container
{
    display: inline;
}

.notification {
  position: relative;
  display: inline-block;
}

.notification .badge {
  position: absolute;
  top: -7px;
  right: -7px;
}
.list-group-item+.list-group-item.active {
    margin-top: 0px;
}
</style>
@endsection

@section('content')
<div class="row">
    <div class="col-md-12">
        <div class="card card-primary">
            <div class="card-header border-bottom">
                <h3 class="card-title"><b>User Detail</b></h3>
            </div>

            <div class="card-body" style="background-color:#f1f3f4;">
                <div class="row">
                    <div class="col-xl-4 col-lg-4 col-md-12 mt-3">
                        <div class="card">
                            <div class="card-body text-center rounded" style="background: linear-gradient(90deg, #1f93c4 0%, #2a3b8f 100%);">
                                <div class="row">
                                    <div class="col-lg-6 col-md-6 col-sm-12">
                                        <div class="widget-user-image overflow-hidden mx-auto mt-3 mb-4">
                                            <img alt="User Avatar" class="rounded-circle mx-auto" src="@if($data->image) @if(substr($data->image, 0, 4)=='http') {{$data->image}} @else {{asset('uploads/'.$data->image)}} @endif @else {{asset('assets/images/no-user.jpg')}} @endif" height="80px" width="80px">
                                        </div>
                                        <h4 class="mb-2 mt-2 font-weight-800 text-light text-shadow">{{$data->name}}</h4>
                                        <h6 class="text-white text-shadow mb-3" style="font-size:12px;"><b>{{$data->email}}</b></h6>
                                    </div>
                                    <div class="col-lg-6 col-md-6 col-sm-12 mt-4">
                                        <h4 class="mt-7 font-weight-800 text-light text-shadow" style="font-size:16px;">{{$data->words_left}}</h4>
                                        <h6 class="text-white mb-4 text-shadow" style="font-size:12px;"><b>Words Left</b></h6>
                                        
                                        <h4 class="mt-7 font-weight-800 text-light text-shadow fs-16" style="font-size:16px;">{{$data->image_left}}</h4>
                                        <h6 class="text-white mb-4 text-shadow" style="font-size:12px;"><b>Images Left</b></h6>
                                        
                                        <span class="btn btn-primary rounded-pill" style="font-size:12px;"><i class="fa-sharp fa-solid fa-crown text-yellow mr-2"></i><b>{{($data->subscription_id)?$data->plan->plan_name:"Trial Plan"}}</b></span><br>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xl-8 col-lg-8 col-md-12 mt-3">
                        <div class="row">
                            <div class="col-lg-12 col-md-12 col-sm-12">
                                <div class="row">
                                    <div class="col-lg-6 col-md-12 col-sm-12">
                                        <div class="card overflow-hidden shadow">
                                            <div class="card-body row" style="background-image: linear-gradient(to right,#2dcebb, #2dce8f);">
                                                <div class="usage-info w-100 col-10">
                                                    <div class="mb-3 font-weight-bold text-light" style="font-size:16px;">Documents Created <span>(This Month)</span></div>
                                                    <h2 class="number-font text-light" style="font-size:20px;">{{$documents_created}} <span style="font-size:18px;">contents</span></h2>
                                                </div>
                                                <div class="col-2">
                                                    <i class="fa-solid fa-file mt-3 text-light" style="font-size:30px;"></i>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-lg-6 col-md-12 col-sm-12">
                                        <div class="card overflow-hidden border-0 shadow">
                                            <div class="card-body row" style="background-image: linear-gradient(to right,#f53a58, #f5583e);">
                                                <div class="usage-info w-100 col-10">
                                                    <p class="mb-3 font-weight-bold text-light" style="font-size:16px;">Words Generated <span>(This Month)</span></p>
                                                    <h2 class="number-font text-light" style="font-size:20px;">{{$documents_word}} <span style="font-size:18px;">words</span></h2>
                                                </div>
                                                <div class="col-2">
                                                    <i class="fa-solid fa-file-word mt-3 text-light" style="font-size:30px;"></i>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-lg-6 col-md-12 col-sm-12">
                                        <div class="card overflow-hidden border-0 shadow">
                                            <div class="card-body row" style="background-image: linear-gradient(to right,#fba140, #fb6d40);">
                                                <div class="usage-info w-100 col-10">
                                                    <p class="mb-3 font-weight-bold text-light" style="font-size:16px;">Images Created <span>(This Month)</span></p>
                                                    <h2 class="number-font text-light" style="font-size:20px;">{{$images_created}} <span style="font-size:18px;">images</span></h2>
                                                </div>
                                                <div class="col-2">
                                                    <i class="fa-solid fa-image mt-3 text-light" style="font-size:30px;"></i>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-lg-6 col-md-12 col-sm-12">
                                        <div class="card overflow-hidden border-0 shadow">
                                            <div class="card-body row" style="background-image: linear-gradient(to right,#7963e4, #6270e4);">
                                                <div class="usage-info w-100 col-10">
                                                    <p class="mb-3 font-weight-bold text-light" style="font-size:16px;">Templates Used <span>(This Month)</span></p>
                                                    <h2 class="number-font text-light" style="font-size:20px;">{{$templates_used}} / <?php echo App\Models\Templates::count(); ?></h2>
                                                </div>
                                                <div class="col-2">
                                                    <i class="fa-solid fa-cloud mt-3 text-light" style="font-size:30px;"></i>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-lg-8 mt-2">
                        <div class="card card-light shadow bg-white rounded" style="margin-top:50px;">
                            <div class="card-body">
                                @if (count($errors) > 0)
                                <div class="alert alert-danger">
                                    <ul>
                                        @foreach ($errors->all() as $error)
                                        <li>{{ $error }}</li>
                                        @endforeach
                                    </ul>
                                </div>
                                @endif
                                <div class="container-fluid">
                                    <div class="row vertab-container">
                                        @if($data->user_type != "Super Admin")
                                        <div class="col-lg-3 col-md-4 vertab-menu">
                                            <div class="list-group">
                                                <a href="#" class="list-group-item text-left"><img type="image" class="mr-2" src="{{asset('assets/images/add_user_Icon/Edit profile_icon.svg')}}" alt="Image" style="display: inline-block;">Edit Profile</a>
                                                <!-- <a href="#" class="list-group-item text-left"><img type="image" class="mr-2" src="{{asset('assets/images/add_user_Icon/Subscription_Icon.svg')}}" alt="Image" style="display: inline-block;">Subscription</a> -->
                                                <a href="#" class="list-group-item text-left"><img type="image" class="mr-2" src="{{asset('assets/images/add_user_Icon/Transication.svg')}}" alt="Image" style="display: inline-block;">Transaction</a>
                                                <a href="#" class="list-group-item text-left"><i class="fa-regular fa-file mr-2 text-primary"></i> Documents</a>
                                                <a href="#" class="list-group-item text-left"><i class="fa-solid fa-image mr-2 text-primary"></i> Images</a>
                                            </div>
                                        </div>
                                        @endif
                                        <div id="accordion" class="col-lg-8 col-md-8 vertab-accordion panel-group"> 
                                            <div class="vertab-content">
                                                <div class="panel-heading">
                                                    <h4 class="panel-title" data-toggle="collapse" data-parent="#accordion" data-target="#collapse1">
                                                        Edit Profile
                                                    </h4>
                                                </div>
                                                <div id="collapse1" class="panel-collapse collapse">
                                                    <div class="panel-body">
                                                        {!! Form::open(['route' => ['user.update',$data->id],'method'=>'PATCH','files'=>true]) !!}
                                                        {!! Form::hidden('id',$data->id) !!}

                                                        @if (count($errors) > 0)
                                                            <div class="alert alert-danger">
                                                                <ul>
                                                                    @foreach ($errors->all() as $error)
                                                                    <li>{{ $error }}</li>
                                                                    @endforeach
                                                                </ul>
                                                            </div>
                                                        @endif

                                                        <div class="row mt-3">
                                                            <div class="col-12">
                                                                <div class="form-group row">
                                                                    {!! Form::label('name','Name', ['class' => 'col-xl-3 col-md-3 col-form-label']) !!}
                                                                    <div class="col-xl-9 col-md-9">
                                                                        {!! Form::text('name',$data->name,['class' => 'form-control','required']) !!}
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        
                                                        <div class="row">
                                                            <div class="col-12">
                                                                <div class="form-group row">
                                                                    {!! Form::label('email','Email', ['class' => 'col-xl-3 col-md-3 col-form-label']) !!}
                                                                    <div class="col-xl-9 col-md-9">
                                                                        {!! Form::email('email',$data->email,['class'=>'form-control','required']) !!}
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div class="row">
                                                            <div class="col-12">
                                                                <div class="form-group row">
                                                                    {!! Form::label('words_left','Available Word', ['class' => 'col-xl-3 col-md-3 col-form-label']) !!}
                                                                    <div class="col-xl-9 col-md-9">
                                                                        {!! Form::number('words_left',$data->words_left,['class' => 'form-control','required']) !!}
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div class="row">
                                                            <div class="col-12">
                                                                <div class="form-group row">
                                                                    {!! Form::label('image_left','Available Image', ['class' => 'col-xl-3 col-md-3 col-form-label']) !!}
                                                                    <div class="col-xl-9 col-md-9">
                                                                        {!! Form::number('image_left',$data->image_left,['class' => 'form-control','required']) !!}
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div> 

                                                        <div class="row">
                                                            <div class="col-12">
                                                                <div class="form-group row">
                                                                    {!! Form::label('start_date','Start Date', ['class' => 'col-xl-3 col-md-3 col-form-label']) !!}
                                                                    <div class="col-xl-9 col-md-9">
                                                                        {!! Form::text('start_date',($data->subscription_start_date)?date('d M, y',strtotime($data->subscription_start_date)):"",['class' => 'form-control datepicker','required','placeholder'=>'Ex 02 Jan, 22',"autocomplete"=>"off"]) !!}
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div class="row">
                                                            <div class="col-12">
                                                                <div class="form-group row">
                                                                    {!! Form::label('end_date','End Date', ['class' => 'col-xl-3 col-md-3 col-form-label']) !!}
                                                                    <div class="col-xl-9 col-md-9">
                                                                        {!! Form::text('end_date',($data->subscription_end_date)?date('d M, y',strtotime($data->subscription_end_date)):"",['class' => 'form-control datepicker','required','placeholder'=>'Ex 02 Jan, 22',"autocomplete"=>"off"]) !!}
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div class="row">
                                                            <div class="col-12">
                                                                <div class="form-group row">
                                                                    {!! Form::label('password', 'Password', ['class' => 'col-xl-3 col-md-3 col-form-label']) !!}
                                                                    <div class="col-xl-9 col-md-9">
                                                                        {!! Form::password('password', ['class' => 'form-control']) !!}
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div class="row">
                                                            <div class="col-12">
                                                                <div class="form-group row">
                                                                    {!! Form::label('image','Select Image', ['class' => 'col-xl-3 col-md-3 col-form-label']) !!}
                                                                    <div class="col-xl-9 col-md-9">
                                                                        {!! Form::file('image', null,['class' => 'form-control','required']) !!}
                                                                        @if($data->image)
                                                                        <img src="@if(substr($data->image, 0, 4)=="http") {{$data->image}} @else {{asset('uploads/'.$data->image)}} @endif" class="mt-2" width="100px" height="100px">
                                                                        @endif
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div class="row">
                                                            <div class="col-md-12 text-center">
                                                            @if(Auth::user()->user_type == "Demo")
                                                            <button type="button" class="btn btn-success ToastrButton mb-3 mb-md-0">Save</button>
                                                            @else
                                                            {!! Form::submit('Save', ['class' => 'btn btn-success mb-3 mb-md-0']) !!}
                                                            @endif
                                                            </div>
                                                        </div>
                                                        {!! Form::close() !!}
                                                    </div>
                                                </div>
                                            </div>
                                            
                                            <!-- <div class="vertab-content">
                                                <div class="panel-heading">
                                                    <h4 class="panel-title" data-toggle="collapse" data-parent="#accordion" data-target="#collapse3">
                                                        Subscription
                                                    </h4>
                                                </div>
                                                <div id="collapse3" class="panel-collapse collapse">
                                                    <div class="panel-body">
                                                        {!! Form::open(['url' => 'admin/subscription-update','method'=>'post','files'=>true]) !!}
                                                        {!! Form::hidden('id',$data->id)!!}
                                                        @if (count($errors) > 0)
                                                            <div class="alert alert-danger">
                                                                <ul>
                                                                    @foreach ($errors->all() as $error)
                                                                    <li>{{ $error }}</li>
                                                                    @endforeach
                                                                </ul>
                                                            </div>
                                                        @endif
                                                        <div class="row">
                                                            <div class="col-12">
                                                                <div class="form-group row">
                                                                    {!! Form::label('plan','Select Plan', ['class' => 'col-sm-4 col-form-label']) !!}
                                                                    <div class="col-sm-8">
                                                                        <select id="plan" name="plan" class="form-control" required>
                                                                            <option value="">Select Plan</option>
                                                                            @foreach($subscription as $sub)
                                                                            <option value="{{$sub->id}}" @if($sub->id == $data->subscription_id) selected @endif>{{$sub->plan_name}}</option>
                                                                            @endforeach
                                                                        </select>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div class="row">
                                                            <div class="col-12">
                                                                <div class="form-group row">
                                                                    {!! Form::label('subscription_start_date','Subscription Start Date', ['class' => 'col-sm-4 col-form-label']) !!}
                                                                    <div class="col-sm-8">
                                                                        {!! Form::text('subscription_start_date',($data->subscription_start_date)?date('d M, y',strtotime($data->subscription_start_date)):"",['class' => 'form-control datepicker','required','placeholder'=>'Ex 02 Jan, 22',"autocomplete"=>"off"]) !!}
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div class="row">
                                                            <div class="col-12">
                                                                <div class="form-group row">
                                                                    {!! Form::label('subscription_end_date','Subscription End Date', ['class' => 'col-sm-4 col-form-label']) !!}
                                                                    <div class="col-sm-8">
                                                                        {!! Form::text('subscription_end_date',($data->subscription_end_date)?date('d M, y',strtotime($data->subscription_end_date)):"",['class' => 'form-control datepicker','required','placeholder'=>'Ex 02 Jan, 22',"autocomplete"=>"off"]) !!}
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div class="row">
                                                            <div class="col-md-12 text-center">
                                                            @if(Auth::user()->user_type == "Demo")
                                                            <button type="button" class="btn btn-success ToastrButton">Save</button>
                                                            @else
                                                            {!! Form::submit('Save', ['class' => 'btn btn-success']) !!}
                                                            @endif
                                                            </div>
                                                        </div>
                                                        {!! Form::close() !!}
                                                    </div>
                                                </div>
                                            </div> -->

                                            <div class="vertab-content">
                                                <div class="panel-heading">
                                                    <h4 class="panel-title" data-toggle="collapse" data-parent="#accordion" data-target="#collapse4">
                                                        Transaction
                                                    </h4>
                                                </div>
                                                <div id="collapse4" class="panel-collapse collapse">
                                                    <div class="panel-body table-responsive">
                                                        <table class="table table-bordered" id="transaction_table">
                                                            <thead class="thead-inverse" style="background-color: #cdf4fa;">
                                                                <tr>
                                                                    <th>#</th>
                                                                    <th>Payment</th>
                                                                    <!-- <th>Payment Type</th> -->
                                                                    <th>Date</th>
                                                                    <th>Status</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                @foreach($transaction as $t)
                                                                <tr>
                                                                    <td>{{$t->id}}</td>
                                                                    <td>{{$t->total_paid}} USD</td>
                                                                    <!-- <td>{{$t->payment_type}}</td> -->
                                                                    <td>{{date('d M, y',strtotime($t->date))}}</td>
                                                                    <td>{{$t->status}}</td>
                                                                </tr>
                                                                @endforeach
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="vertab-content">
                                                <div class="panel-heading">
                                                    <h4 class="panel-title" data-toggle="collapse" data-parent="#accordion" data-target="#collapse5">
                                                        Documents
                                                    </h4>
                                                </div>
                                                <div id="collapse5" class="panel-collapse collapse">
                                                    <div class="panel-body table-responsive">
                                                        <table class="table table-bordered" id="document_table">
                                                            <thead class="thead-inverse" style="background-color: #cdf4fa;">
                                                                <tr>
                                                                    <th>#</th>
                                                                    <th>Name</th>
                                                                    <th>Category</th>
                                                                    <th>Action</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                @foreach($document as $d)
                                                                <tr>
                                                                    <td>{{$d->id}}</td>
                                                                    <td>{{$d->name}}</td>
                                                                    <td>{{$d->templates->type}}</td>
                                                                    <td class="text-center">
                                                                        <a onclick="exportWord(this);" style="cursor:pointer;" data-value="{{$d->text}}" data-name="{{$d->name}}"><span aria-hidden="true" class="fa fa-download fa-xl mr-3 text-primary"></span></a>
                                                                        <a data-id="{{$d->id}}" data-toggle="modal" data-target="#documentModal"><span aria-hidden="true" class="fa fa-trash fa-xl text-primary"></span></a>
                                                                    </td>
                                                                    {!! Form::open(['url' => 'admin/document-delete','method'=>'POST','class'=>'form-horizontal','id'=>'docs_form_'.$d->id]) !!}
                                                                    {!! Form::hidden("id",$d->id) !!}
                                                                    {!! Form::close() !!}
                                                                </tr>
                                                                @endforeach
                                                            </tbody>
                                                        </table>
                                                        
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="vertab-content">
                                                <div class="panel-heading">
                                                    <h4 class="panel-title" data-toggle="collapse" data-parent="#accordion" data-target="#collapse6">
                                                        Images
                                                    </h4>
                                                </div>
                                                <div id="collapse6" class="panel-collapse collapse">
                                                    <div class="panel-body table-responsive">
                                                        <table class="table table-bordered" id="image_table">
                                                            <thead class="thead-inverse" style="background-color: #cdf4fa;">
                                                                <tr>
                                                                    <th>#</th>
                                                                    <th>Image</th>
                                                                    <th>Action</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                @foreach($image as $img)
                                                                <tr>
                                                                    <td>{{$img->id}}</td>
                                                                    <td><img src="{{asset('uploads/aiImage/'.$img->image)}}" class="image ai-image" width="50px" style="border-radius:5px;"></td>
                                                                    <td class="text-center">
                                                                        <div style="display:inline-flex;">
                                                                            <span class="download text-primary" onclick="getFile(this);" data-value="{{asset('uploads/aiImage/'.$img->image)}}" style='cursor:pointer;'><i class="fa-solid fa-cloud-arrow-down fa-xl mr-3"></i></span>
                                                                            <a data-id="{{$img->id}}" data-toggle="modal" data-target="#imageModal"><span aria-hidden="true" class="fa fa-trash fa-xl text-primary"></span></a>
                                                                        </div>
                                                                    </td>
                                                                    {!! Form::open(['url' => 'admin/ai-image-delete','method'=>'POST','class'=>'form-horizontal','id'=>'form_'.$img->id]) !!}
                                                                    {!! Form::hidden("id",$img->id) !!}
                                                                    {!! Form::close() !!}
                                                                </tr>
                                                                @endforeach
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-lg-4 mt-2">
                        <div class="card card-light shadow bg-white rounded userCard" style="margin-top:50px;">
                            <div class="w-100">
                                <img src="@if($data->image) @if(substr($data->image, 0, 4)=="http") {{$data->image}} @else {{asset('uploads/'.$data->image)}} @endif @else {{asset('assets/images/no-user.jpg')}} @endif" alt="Profile Image" class="rounded-circle profile_img shadow bg-white rounded">
                                <div class="card-body">
                                    
                                    <div class="row mt-5">
                                        <div class="col-12">
                                            <div class="form-group row">
                                                {!! Form::label('name','Name', ['class' => 'col-sm-4 col-form-label text-primary']) !!}
                                                <span class="my-auto col-sm-8">{{$data->name}}</sapn>
                                            </div>
                                        </div>
                                    </div>
                                
                                    <div class="row">
                                        <div class="col-12">
                                            <div class="form-group row">
                                                {!! Form::label('email','Email', ['class' => 'col-sm-4 col-form-label text-primary']) !!}
                                                <span class="my-auto col-sm-8">{{$data->email}}</sapn>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- <div class="row">
                                        <div class="col-12">
                                            <div class="form-group row">
                                                {!! Form::label('mobile_no','Mobile No', ['class' => 'col-sm-4 col-form-label text-primary']) !!}
                                                <span class="my-auto col-sm-8">{{$data->mobile_no}}</sapn>
                                            </div>
                                        </div>
                                    </div> -->

                                    <div class="row">
                                        <div class="col-12">
                                            <div class="form-group row">
                                                {!! Form::label('created_at','Entry', ['class' => 'col-sm-4 col-form-label text-primary']) !!}
                                                <span class="my-auto col-sm-8">{{$data->created_at->format('d/m/Y')}}</sapn>
                                            </div>
                                        </div>
                                    </div>

                                    @if($data->user_type != "Super Admin")
                                    <div class="row">
                                        <div class="col-12">
                                            <div class="form-group row">
                                                {!! Form::label('current_plan','Current Plan', ['class' => 'col-sm-4 col-form-label text-primary']) !!}
                                                <span class="my-auto col-sm-8">@if($data->subscription_id){{$data->plan->plan_name}}@else Trial Plan @endif</sapn>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-12">
                                            <div class="form-group row">
                                                {!! Form::label('start_date','Start Date', ['class' => 'col-sm-4 col-form-label text-primary']) !!}
                                                <span class="my-auto col-sm-8">@if($data->subscription_start_date){{date('d M, y',strtotime($data->subscription_start_date))}}@endif</sapn>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-12">
                                            <div class="form-group row">
                                                {!! Form::label('end_date','End Date', ['class' => 'col-sm-4 col-form-label text-primary']) !!}
                                                <span class="my-auto col-sm-8">@if($data->subscription_end_date){{date('d M, y',strtotime($data->subscription_end_date))}}@endif</sapn>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-12">
                                            <div class="form-group row">
                                                {!! Form::label('available_word','Available Word', ['class' => 'col-sm-4 col-form-label text-primary']) !!}
                                                <span class="my-auto col-sm-8">{{$data->words_left}}</sapn>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-12">
                                            <div class="form-group row">
                                                {!! Form::label('available_image','Available Image', ['class' => 'col-sm-4 col-form-label text-primary']) !!}
                                                <span class="my-auto col-sm-8">{{$data->image_left}}</sapn>
                                            </div>
                                        </div>
                                    </div>
                                    @endif
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- image Modal -->
<div id="documentModal" class="modal fade" role="dialog">
    <div class="modal-dialog">
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <h4 class="modal-title">Delete</h4>
          <button type="button" class="close" data-dismiss="modal">&times;</button>
        </div>
        <div class="modal-body">
          <p>Are you sure you want to Delete ?</p>
        </div>
        <div class="modal-footer">
          @if(Auth::user()->user_type == "Demo")
          <button type="button" class="btn btn-danger ToastrButton">Delete</button>
          @else
          <button id="doc_del_btn" class="btn btn-danger" type="button" data-submit="">Delete</button>
          @endif
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
</div>
<!-- Modal -->
  
<!-- image Modal -->
<div id="imageModal" class="modal fade" role="dialog">
    <div class="modal-dialog">
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <h4 class="modal-title">Delete</h4>
          <button type="button" class="close" data-dismiss="modal">&times;</button>
        </div>
        <div class="modal-body">
          <p>Are you sure you want to Delete ?</p>
        </div>
        <div class="modal-footer">
          @if(Auth::user()->user_type == "Demo")
          <button type="button" class="btn btn-danger ToastrButton">Delete</button>
          @else
          <button id="del_btn" class="btn btn-danger" type="button" data-submit="">Delete</button>
          @endif
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>
  <!-- Modal -->
@endsection

@section("script")
<script type="text/javascript"> 
    $('#plan').select2();
    $('.plusButton').css('cursor', 'pointer');

    $('.datepicker').datepicker({
        dateFormat: 'dd M, y',
        minDate:'today',  
    });

    $('#transaction_table').dataTable({
        "paging": true,
        "dom": 'tp',
        "order": [[0, 'desc']],
    });

    $('#document_table').dataTable({
        "paging": true,
        "dom": 'tp',
        "order": [[0, 'desc']],
    });

    $('#image_table').dataTable({
        "paging": true,
        "dom": 'tp',
        "order": [[0, 'desc']],
    });

    $('.datepicker').datepicker({
        dateFormat: 'dd M, y',
        minDate:'today',  
    });
    
    $("#doc_del_btn").on("click",function(){
        var id=$(this).data("submit");
        $("#docs_form_"+id).submit();
    });

    $('#documentModal').on('show.bs.modal', function(e) {
        var id = e.relatedTarget.dataset.id;
        $("#doc_del_btn").attr("data-submit",id);
    });

    $("#del_btn").on("click",function(){
        var id=$(this).data("submit");
        $("#form_"+id).submit();
    });

    $('#imageModal').on('show.bs.modal', function(e) {
        var id = e.relatedTarget.dataset.id;
        $("#del_btn").attr("data-submit",id);
    });

    $(document).ready(function()
    {
        $("#plan").change(function(){
            var value = $(this).val();
            
            $.ajax({
                type: "GET",
                url: "{{url('admin/user-get-plan')}}",
                data: { id : value},
                headers: { 'X-CSRF-TOKEN': $('meta[name="csrf-token"]').attr('content') },
                success: function(data) {
                $("#subscription_start_date").val(data['start_date']);
                $("#subscription_end_date").val(data['end_date']);
                },
            });
        });
    });

    function exportWord(e)
    {
      var text = jQuery(e).data("value");
      var name = jQuery(e).data("name");
      var header = "<html xmlns:o='urn:schemas-microsoft-com:office:office' "+
          "xmlns:w='urn:schemas-microsoft-com:office:word' "+
          "xmlns='http://www.w3.org/TR/REC-html40'>"+
          "<head><meta charset='utf-8'><title>Export HTML to Word Document with JavaScript</title></head><body>";

      var footer = "</body></html>";
      var sourceHTML = header+text+footer;
      
      var source = 'data:application/vnd.ms-word;charset=utf-8,' + encodeURIComponent(sourceHTML);
      var fileDownload = document.createElement("a");
      document.body.appendChild(fileDownload);
      fileDownload.href = source;
      fileDownload.download = name+'.doc';
      fileDownload.click();
      document.body.removeChild(fileDownload);

      new PNotify({
        title: 'Success!',
        text: "Word document was created successfully.",
        type: 'success'
      });
    }

    function getFile(e) {
        var uri = jQuery(e).data("value");
		var link = document.createElement("a");
        link.href = uri;
        link.setAttribute("download", "download");
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        delete link;
		return false;
	}
</script>

<script>
// Screen-width breakpoint
var tc_breakpoint = 767;

jQuery(document).ready(function() 
{
	"use strict";
	
	// Switch tabs and update panels classes - Adjust container height
    jQuery(".vertab-container .vertab-menu .list-group a").click(function(e) 
	{
        
        var index = jQuery(this).index();
		var container = jQuery(this).parents('.vertab-container');
		var accordion = container.find('.vertab-accordion');
		var contents = accordion.find(".vertab-content");
		
		e.preventDefault();
		
        jQuery(this).addClass("active");
        jQuery(this).siblings('a.active').removeClass("active");
        
		contents.removeClass("active");
        contents.eq(index).addClass("active");
		container.data('current',index);
        //alert(jQuery(container).height());
		//Adjust container height
		//jQuery(this).parents('.vertab-menu').css('min-height',jQuery(container).children('.vertab-accordion').height());
    });
	
	// Collapse accordion panels (except the one the user just opened) and add "active" class to the panel heading 
	jQuery('.vertab-accordion').on('show.bs.collapse','.collapse', function() 
	{
		var accordion, container, current, index;
		
		accordion = jQuery(this).parents('.vertab-accordion');
		container = accordion.parents('.vertab-container');
		
		accordion.find('.collapse.in').each(function()
		{
			jQuery(this).collapse('hide');
		});		
		
		jQuery(this).siblings('.panel-heading').addClass('active');
		
		current = accordion.find('.panel-heading.active');
		index = accordion.find('.panel-heading').index(current);
		
		container.data('current',index);
	});
								   
	// Remove "active" class from heading when collapsing the current panel 
	jQuery('.vertab-accordion .panel-collapse').on('hide.bs.collapse', function () {
		jQuery(this).siblings('.panel-heading').removeClass('active');
	});	
	
	// Manage resize / rotation events
	jQuery( window ).on( "resize orientationchange", function(  ) 
	{
		resize_vertical_accordions();
	});
	
	// Scroll accordion to show the current panel
	jQuery(".vertab-accordion .panel-heading").click(function () 
	{
		var el = this;
		setTimeout(function(){jQuery("html, body").animate({scrollTop: jQuery(el).offset().top - 10 }, 1000);},500);
		
		return true;
	});
	
	//Initial Panels setup
	resize_vertical_accordions(  );
});

function resize_vertical_accordions(  ) 
{
	"use strict";
	jQuery('.vertab-container').each(function(i, e)
	{
		var index, menu, contents; 
		var container = jQuery(this);
		
		// Setup current tab/panel (default to first tab/panel)
		index = jQuery(this).data('current');
		if(index === undefined)
		{
			jQuery(this).data('index',0);
			index = 0;
		}
		
		// If using a desktop-size screen, manage as tabbed panels
		if( jQuery( window ).width() > tc_breakpoint)
		{
			// Reset panels heights (Bootstrap's accordions sets heights to zero)
			jQuery(this).find('.panel-collapse.collapse').css('height','auto');
			
			// Clean tab-navigation styles
			menu = jQuery(this).find('.vertab-menu .list-group a');
			menu.removeClass("active");

			// Clean tab-panels styles
			contents = jQuery(this).find(".vertab-accordion .vertab-content");
			contents.removeClass("active");
			
			// Update tab navigation and panels styles
			menu.eq(index).addClass('active');			
			contents.eq(index).addClass("active");
			
			// Update tab navigation's height to match current tab
			jQuery(this).children('.vertab-menu').css('min-height',jQuery(this).children('.vertab-accordion').height());			
		}
		else // If using a mobile device (phone + tablets), manage as accordion
		{
			// Close all panels
			jQuery(this).find('.vertab-content .panel-collapse.collapse').collapse('hide');
			
			// Clean styles from headings
			jQuery(this).find('.vertab-content .panel-heading').removeClass('active');
			
			// Wait until all panels have collapsed and mark the one the user selected as active.
			setTimeout(function()
			{
				jQuery(container).find('.vertab-content .panel-heading').eq(index).addClass("active");
				jQuery(container).find('.vertab-content .panel-collapse.collapse').eq(index).collapse('show');				
			},1000);

		}	
	});	
}
</script>
@endsection