@extends('layouts.master')

@section('extra_css')
<link href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.11/summernote-lite.css" rel="stylesheet">
<link rel="stylesheet" href="{{ asset('assets/css/clean-switch.css')}}">
<style type="text/css">
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
.select2-container{
    display: inline;
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
.list-group-item+.list-group-item.active {
    margin-top: 0px;
}
img{
    display: inline;
}
</style>
@endsection

@section('content')
<div class="row">
    <div class="col-md-12">
        <div class="card card-primary">
            <div class="card-header border-bottom">
                <h3 class="card-title"><b>Setting</b></h3>
            </div>

            <div class="card-body">
                <div class="row">
                    <div class="col-md-12 mt-2">
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
                                <div class="col-lg-2 col-md-3 col-sm-12 vertab-menu">
                                    <div class="list-group">
                                        <a href="#" class="list-group-item text-left"><img type="image" class="mr-2" src="{{asset('assets/images/Setting Icon/App Setting.svg')}}" alt="Image">App Setting</a>
                                        <a href="#" class="list-group-item text-left"><i class="fas fa-ad fa-lg mr-1 text-primary"></i>Ads Setting</a>
                                        <a href="#" class="list-group-item text-left"><i class="fas fa-shield-alt fa-md mr-2 text-primary"></i>OpenAI Setting</a>
                                        <a href="#" class="list-group-item text-left"><img type="image" class="mr-2" src="{{asset('assets/images/Setting Icon/Payment Setting.svg')}}" alt="Image">Payment Setting</a>
                                        <a href="#" class="list-group-item text-left"><img type="image" class="mr-2" src="{{asset('assets/images/Setting Icon/Email Setting.svg')}}" alt="Image">Email Setting</a>
                                        <a href="#" class="list-group-item text-left"><i class="fas fa-receipt fa-md mr-2 text-primary"></i>Invoice Setting</a>
                                        <a href="#" class="list-group-item text-left"><img type="image" class="mr-2" src="{{asset('assets/images/Setting Icon/App Upadate Popup.svg')}}" alt="Image">App Update Popup</a>
                                        <a href="#" class="list-group-item text-left"><i class="fas fa-circle-info fa-md mr-2 text-primary"></i>About Us</a>
                                        <a href="#" class="list-group-item text-left"><img type="image" class="mr-2" src="{{asset('assets/images/Setting Icon/Privacy Policy.svg')}}" alt="Image">Privacy Policy</a>
                                        <a href="#" class="list-group-item text-left"><img type="image" class="mr-2" src="{{asset('assets/images/Setting Icon/Refund Policy.svg')}}" alt="Image">Refund Policy</a>
                                        <a href="#" class="list-group-item text-left"><img type="image" class="mr-2" src="{{asset('assets/images/Setting Icon/Terms & Condition.svg')}}" alt="Image">Terms & Condition</a>
                                    </div>
                                </div>
                                <div id="accordion" class="col-lg-10 col-md-9 col-sm-12 vertab-accordion panel-group"> 
                                    <div class="vertab-content">
                                        <div class="panel-heading">
                                            <h4 class="panel-title" data-toggle="collapse" data-parent="#accordion" data-target="#collapse1">
                                                App Setting
                                            </h4>
                                        </div>
                                        <div id="collapse1" class="panel-collapse collapse">
                                            <div class="panel-body">
                                                {!! Form::open(['url' =>'admin/app-setting','method'=>'POST','files'=>true]) !!}
                                                <div class="row">
                                                    <div class="col-12">
                                                        <div class="form-group row">
                                                            {!! Form::label('black_logo','Black Logo', ['class' => 'col-xl-2 col-md-3 col-form-label']) !!}
                                                            <div class="col-xl-10 col-md-9">
                                                                <div class="row">
                                                                    <div class="col-xl-3"><input class="form-control" type="file" id="black_logo" name="name[black_logo]"></div>
                                                                    <div class="col-xl-9" id="preview"><img type="image" class="shadow bg-white rounded" src="@if(App\Models\Setting::getSetting('black_logo')){{asset('uploads/'.App\Models\Setting::getSetting('black_logo'))}}@else{{asset('assets/images/no-image.png')}}@endif" alt="Image" style="width: auto;height: 70px" /></div>  
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-12">
                                                        <div class="form-group row">
                                                            {!! Form::label('white_logo','White Logo', ['class' => 'col-xl-2 col-md-3 col-form-label']) !!}
                                                            <div class="col-xl-10 col-md-9">
                                                                <div class="row">
                                                                    <div class="col-xl-3"><input class="form-control" type="file" id="white_logo" name="name[white_logo]"></div>
                                                                    <div class="col-xl-9" id="preview1"><img type="image" class="shadow bg-white rounded" src="@if(App\Models\Setting::getSetting('white_logo')){{asset('uploads/'.App\Models\Setting::getSetting('white_logo'))}}@else{{asset('assets/images/no-image.png')}}@endif" alt="Image"  style="width: auto;height: 70px" /></div>  
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-12">
                                                        <div class="form-group row">
                                                            {!! Form::label('favicon','Favicon', ['class' => 'col-xl-2 col-md-3 col-form-label']) !!}
                                                            <div class="col-xl-10 col-md-9">
                                                                <div class="row">
                                                                    <div class="col-xl-3"><input class="form-control" type="file" id="favicon" name="name[favicon]"></div>
                                                                    <div class="col-xl-9" id="preview2"><img type="image" class="shadow bg-white rounded" src="@if(App\Models\Setting::getSetting('favicon')){{asset('uploads/'.App\Models\Setting::getSetting('favicon'))}}@else{{asset('assets/images/no-image.png')}}@endif" alt="Image" style="width: 50px;height: 50px" /></div>  
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-12">
                                                        <div class="form-group row">
                                                            {!! Form::label('app_name', 'Name', ['class' => 'col-xl-2 col-md-3 col-form-label']) !!}
                                                            <div class="col-xl-6 col-md-4">
                                                                {!! Form::text('name[app_name]',App\Models\Setting::getSetting('app_name'),['class' => 'form-control','required']) !!}
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-12">
                                                        <div class="form-group row">
                                                            {!! Form::label('email', 'Email', ['class' => 'col-xl-2 col-md-3 col-form-label']) !!}
                                                            <div class="col-xl-6 col-md-4">
                                                                {!! Form::email('name[email]',App\Models\Setting::getSetting('email'),['class' => 'form-control','required']) !!}
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-12">
                                                        <div class="form-group row">
                                                            {!! Form::label('phone_no', 'Phone No', ['class' => 'col-xl-2 col-md-3 col-form-label']) !!}
                                                            <div class="col-xl-6 col-md-4">
                                                                {!! Form::number('name[phone_no]',App\Models\Setting::getSetting('phone_no'),['class' => 'form-control','required']) !!}
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-12">
                                                        <div class="form-group row">
                                                            {!! Form::label('address','Address', ['class' => 'col-xl-2 col-md-3 col-form-label']) !!}
                                                            <div class="col-xl-6 col-md-4">
                                                                <textarea name="name[address]" id="address" class="form-control" cols="5" required>{!! App\Models\Setting::getSetting('address') !!}</textarea>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-12">
                                                        <div class="form-group row">
                                                            {!! Form::label('description','Description', ['class' => 'col-xl-2 col-md-3 col-form-label']) !!}
                                                            <div class="col-xl-6 col-md-4">
                                                                <textarea name="name[description]" id="desc_text" class="form-control" rows="5" required>{!! App\Models\Setting::getSetting('description') !!}</textarea>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                              
                                                @if(Auth::user()->user_type != "Demo")
                                                <div class="row">
                                                    <div class="col-12">
                                                        <div class="form-group row">
                                                            {!! Form::label('api_key', 'Api Key', ['class' => 'col-xl-2 col-md-3 col-form-label']) !!}
                                                            <div class="col-xl-6 col-md-4">
                                                                {!! Form::text('name[api_key]',App\Models\Setting::getSetting('api_key'),['class' => 'form-control','required']) !!}
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                @endif

                                                <div class="row">
                                                    <div class="col-12">
                                                        <div class="form-group row">
                                                            {!! Form::label('facebook', 'Facebook', ['class' => 'col-xl-2 col-md-3 col-form-label']) !!}
                                                            <div class="col-xl-6 col-md-4">
                                                                {!! Form::text('name[facebook]',App\Models\Setting::getSetting('facebook'),['class' => 'form-control','required']) !!}
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-12">
                                                        <div class="form-group row">
                                                            {!! Form::label('twitter', 'Twitter', ['class' => 'col-xl-2 col-md-3 col-form-label']) !!}
                                                            <div class="col-xl-6 col-md-4">
                                                                {!! Form::text('name[twitter]',App\Models\Setting::getSetting('twitter'),['class' => 'form-control','required']) !!}
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-12">
                                                        <div class="form-group row">
                                                            {!! Form::label('instagram', 'Instagram', ['class' => 'col-xl-2 col-md-3 col-form-label']) !!}
                                                            <div class="col-xl-6 col-md-4">
                                                                {!! Form::text('name[instagram]',App\Models\Setting::getSetting('instagram'),['class' => 'form-control','required']) !!}
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-12">
                                                        <div class="form-group row">
                                                            {!! Form::label('linkedin', 'Linkedin', ['class' => 'col-xl-2 col-md-3 col-form-label']) !!}
                                                            <div class="col-xl-6 col-md-4">
                                                                {!! Form::text('name[linkedin]',App\Models\Setting::getSetting('linkedin'),['class' => 'form-control','required']) !!}
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-12">
                                                        <div class="form-group row">
                                                            {!! Form::label('youtube', 'Youtube', ['class' => 'col-xl-2 col-md-3 col-form-label']) !!}
                                                            <div class="col-xl-6 col-md-4">
                                                                {!! Form::text('name[youtube]',App\Models\Setting::getSetting('youtube'),['class' => 'form-control','required']) !!}
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-12">
                                                        <div class="form-group row">
                                                            {!! Form::label('header_script', 'Header Script', ['class' => 'col-xl-2 col-md-3 col-form-label']) !!}
                                                            <div class="col-xl-6 col-md-4">
                                                                {!! Form::textarea('name[header_script]',App\Models\Setting::getSetting('header_script'),['class' => 'form-control','rows'=>4,'required']) !!}
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    {!! Form::label('footer_text', 'Footer', ['class' => 'col-xl-2 col-md-3 col-form-label']) !!}
                                                    <div class="col-xl-6 col-md-4">
                                                        {!! Form::textarea('name[footer_text]',App\Models\Setting::getSetting('footer_text'),['class' => 'form-control','id'=>'footer_text','rows'=>5,'required']) !!}
                                                    </div>
                                                </div>

                                                <div class="row mt-3">
                                                    <div class="col-12">
                                                        <div class="form-group row">
                                                            {!! Form::label('register_user_default_word','New User Default Word', ['class' => 'col-xl-2 col-md-3 col-form-label']) !!}
                                                            <div class="col-xl-6 col-md-4">
                                                                {!! Form::number('name[register_user_default_word]',App\Models\Setting::getSetting('register_user_default_word'),['class' => 'form-control','required']) !!}
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-12">
                                                        <div class="form-group row">
                                                            {!! Form::label('register_user_default_image','New User Default Image', ['class' => 'col-xl-2 col-md-3 col-form-label']) !!}
                                                            <div class="col-xl-6 col-md-4">
                                                                {!! Form::number('name[register_user_default_image]',App\Models\Setting::getSetting('register_user_default_image'),['class' => 'form-control','required']) !!}
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

                                    <div class="vertab-content">
                                        <div class="panel-heading">
                                            <h4 class="panel-title" data-toggle="collapse" data-parent="#accordion" data-target="#collapse11">
                                                Ads Setting
                                            </h4>
                                        </div>
                                        <div id="collapse11" class="panel-collapse collapse">
                                            <div class="panel-body">
                                                {!! Form::open(['url' =>'admin/ads-setting','method'=>'POST','files'=>true]) !!}

                                                <div class="row mt-3">
                                                    <div class="col-md-6">
                                                        <div class="card card-success">
                                                            <div class="card-header">
                                                                <span>AdMob Settings</span>
                                                            </div>
                                                            <div class="card-body">
                                                                <div class="form-group">
                                                                    <label for="admob_publisher_id">AdMob Publisher Id</label>
                                                                    <input type="text" class="form-control" name="name[admob_publisher_id]" value="{{App\Models\AdsSetting::getAdsSetting('admob_publisher_id')}}" id="admob_publisher_id" placeholder="Enter AdMob Publisher Id">
                                                                </div>
                                                                <div class="form-group">
                                                                    <label for="admob_app_id">AdMob App Id</label>
                                                                    <input type="text" class="form-control" name="name[admob_app_id]" value="{{App\Models\AdsSetting::getAdsSetting('admob_app_id')}}" id="admob_app_id" placeholder="Enter AdMob App Id">
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <div class="col-md-6">
                                                        <div class="card card-success">
                                                            <div class="card-header">
                                                                <span>APP Opens Ads</span>
                                                                <label class="cl-switch cl-switch-blue float-right">
                                                                    <input type="checkbox" id="app_opens_ads_enable" value="1" name="name[app_opens_ads_enable]" @if(App\Models\AdsSetting::getAdsSetting('app_opens_ads_enable')==1) checked @endif>
                                                                    <span class="switcher"></span>
                                                                </label>
                                                            </div>
                                                            <div class="card-body">
                                                                <div class="form-group">
                                                                    <label for="app_open_ads_id">App Open Ads ID</label>
                                                                    <input type="text" class="form-control" name="name[app_open_ads_id]" id="app_open_ads_id" value="{{App\Models\AdsSetting::getAdsSetting('app_open_ads_id')}}" placeholder="Enter App Open Ads Id">
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row mt-3">
                                                    <div class="col-md-6">
                                                        <div class="card card-success">
                                                            <div class="card-header">
                                                                <span>Ad Native</span>
                                                            </div>
                                                            <div class="card-body">
                                                                <div class="form-group">
                                                                    <label for="native_id">Native Id</label>
                                                                    <input type="text" class="form-control" name="name[native_id]" id="native_id" value="{{App\Models\AdsSetting::getAdsSetting('native_id')}}" placeholder="Enter Native Id">
                                                                </div>
                                                                <div class="form-group">
                                                                    <label for="native_display_type">Native Display Type</label>
                                                                    <select id="native_display_type" name="name[native_display_type]" class="form-control" required>
                                                                        <option value="FALSE" @if(App\Models\AdsSetting::getAdsSetting('native_display_type') == "FALSE") selected @endif>Disable Ad Native</option>
                                                                        <option value="ADMOB" @if(App\Models\AdsSetting::getAdsSetting('native_display_type') == "ADMOB") selected @endif>Google AdMob Ad Native</option>
                                                                        <option value="MAX" @if(App\Models\AdsSetting::getAdsSetting('native_display_type') == "MAX") selected @endif>AppLovin Max Ad Native</option>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                
                                                    <div class="col-md-6">
                                                        <div class="card card-success">
                                                            <div class="card-header">
                                                                <span>Ad Banner</span>
                                                            </div>
                                                            <div class="card-body">
                                                                <div class="form-group">
                                                                    <label for="banner_id">Banner ID</label>
                                                                    <input type="text" class="form-control" name="name[banner_id]" value="{{App\Models\AdsSetting::getAdsSetting('banner_id')}}" id="banner_id" placeholder="Enter Banner Id">
                                                                </div>
                                                                <div class="form-group">
                                                                    <label for="banner_display_type">Banner Display Type</label>
                                                                    <select id="banner_display_type" name="name[banner_display_type]" class="form-control" required>
                                                                        <option value="FALSE" @if(App\Models\AdsSetting::getAdsSetting('banner_display_type') == "FALSE") selected @endif>Disable Ad Banner</option>
                                                                        <option value="ADMOB" @if(App\Models\AdsSetting::getAdsSetting('banner_display_type') == "ADMOB") selected @endif>Google AdMob Ad Banner</option>
                                                                        <option value="MAX" @if(App\Models\AdsSetting::getAdsSetting('banner_display_type') == "MAX") selected @endif>AppLovin Max Ad Banner</option>
                                                                        <option value="APPLOVIN" @if(App\Models\AdsSetting::getAdsSetting('banner_display_type') == "APPLOVIN") selected @endif>AppLovin Ad Banner</option>
                                                                        <option value="ironSource" @if(App\Models\AdsSetting::getAdsSetting('banner_display_type') == "ironSource") selected @endif>ironSource Ad Banner</option>
                                                                        <option value="unity" @if(App\Models\AdsSetting::getAdsSetting('banner_display_type') == "unity") selected @endif>Unity Ad Banner</option>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row mt-3">
                                                    <div class="col-md-6">
                                                        <div class="card card-success">
                                                            <div class="card-header">
                                                                <span>Ad Rewarded</span>
                                                            </div>
                                                            <div class="card-body">
                                                                <div class="form-group">
                                                                    <label for="rewarded_id">Rewarded Id</label>
                                                                    <input type="text" class="form-control" name="name[rewarded_id]" id="rewarded_id" value="{{App\Models\AdsSetting::getAdsSetting('rewarded_id')}}" placeholder="Enter Rewarded Id">
                                                                </div>
                                                                <div class="form-group">
                                                                    <label for="rewarded_display_type">Rewarded Display Type</label>
                                                                    <select id="rewarded_display_type" name="name[rewarded_display_type]" class="form-control" required>
                                                                        <option value="FALSE" @if(App\Models\AdsSetting::getAdsSetting('rewarded_display_type') == "FALSE") selected @endif>Disable Ad Rewarded</option>
                                                                        <option value="ADMOB" @if(App\Models\AdsSetting::getAdsSetting('rewarded_display_type') == "ADMOB") selected @endif>Google AdMob Ad Rewarded</option>
                                                                        <option value="MAX" @if(App\Models\AdsSetting::getAdsSetting('rewarded_display_type') == "MAX") selected @endif>AppLovin Max Ad Rewarded</option>
                                                                        <option value="APPLOVIN" @if(App\Models\AdsSetting::getAdsSetting('rewarded_display_type') == "APPLOVIN") selected @endif>AppLovin Ad Rewarded</option>
                                                                        <option value="ironSource" @if(App\Models\AdsSetting::getAdsSetting('rewarded_display_type') == "ironSource") selected @endif>ironSource Ad Rewarded</option>
                                                                        <option value="unity" @if(App\Models\AdsSetting::getAdsSetting('rewarded_display_type') == "unity") selected @endif>Unity Ad Rewarded</option>
                                                                    </select>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label for="daily_limit_rewarded">Daily Limit in Rewarded</label>
                                                                    <input type="number" class="form-control" name="name[daily_limit_rewarded]" id="daily_limit_rewarded" value="{{App\Models\AdsSetting::getAdsSetting('daily_limit_rewarded')}}" placeholder="Enter Daily Limit in Rewarded">
                                                                </div>
                                                                <div class="form-group row">
                                                                    <div class="col-md-6">
                                                                        <label for="rewarded_word">Rewarded Word</label>
                                                                        <input type="number" class="form-control" name="name[rewarded_word]" id="rewarded_word" value="{{App\Models\AdsSetting::getAdsSetting('rewarded_word')}}" placeholder="Enter Rewarded Word">
                                                                    </div>
                                                                    <div class="col-md-6">
                                                                        <label for="rewarded_image">Rewarded Image</label>
                                                                        <input type="number" class="form-control" name="name[rewarded_image]" id="rewarded_image" value="{{App\Models\AdsSetting::getAdsSetting('rewarded_image')}}" placeholder="Enter Rewarded Image">
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <div class="col-md-6">
                                                        <div class="card card-success">
                                                            <div class="card-header">
                                                                <span>Ad Interstitial</span>
                                                            </div>
                                                            <div class="card-body">
                                                                <div class="form-group">
                                                                    <label for="interstitial_id">Interstitial Id</label>
                                                                    <input type="text" class="form-control" name="name[interstitial_id]" id="interstitial_id" value="{{App\Models\AdsSetting::getAdsSetting('interstitial_id')}}" placeholder="Enter Interstitial Id">
                                                                </div>
                                                                <div class="form-group">
                                                                    <label for="interstitial_display_type">Interstitial Display Type</label>
                                                                    <select id="interstitial_display_type" name="name[interstitial_display_type]" class="form-control" required>
                                                                        <option value="FALSE" @if(App\Models\AdsSetting::getAdsSetting('interstitial_display_type') == "FALSE") selected @endif>Disable Ad Interstitial</option>
                                                                        <option value="ADMOB" @if(App\Models\AdsSetting::getAdsSetting('interstitial_display_type') == "ADMOB") selected @endif>Google AdMob Ad Interstitial</option>
                                                                        <option value="MAX" @if(App\Models\AdsSetting::getAdsSetting('interstitial_display_type') == "MAX") selected @endif>AppLovin Max Ad Interstitial</option>
                                                                        <option value="APPLOVIN" @if(App\Models\AdsSetting::getAdsSetting('interstitial_display_type') == "APPLOVIN") selected @endif>AppLovin Ad Interstitial</option>
                                                                        <option value="ironSource" @if(App\Models\AdsSetting::getAdsSetting('interstitial_display_type') == "ironSource") selected @endif>ironSource Ad Interstitial</option>
                                                                        <option value="unity" @if(App\Models\AdsSetting::getAdsSetting('interstitial_display_type') == "unity") selected @endif>Unity Ad Interstitial</option>
                                                                    </select>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label for="click_interstitial_ad">Click between Interstitial Ad</label>
                                                                    <input type="number" class="form-control" name="name[click_interstitial_ad]" id="click_interstitial_ad" value="{{App\Models\AdsSetting::getAdsSetting('click_interstitial_ad')}}">
                                                                </div>
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
                                    </div>

                                    <div class="vertab-content">
                                        <div class="panel-heading">
                                            <h4 class="panel-title" data-toggle="collapse" data-parent="#accordion" data-target="#collapse02">
                                                OpenAI Setting
                                            </h4>
                                        </div>
                                        <div id="collapse02" class="panel-collapse collapse">
                                            <div class="panel-body">
                                            {!! Form::open(['url' =>'admin/openai-setting','method'=>'POST','files'=>true]) !!}

                                            @if(Auth::user()->user_type != "Demo")
                                                <div class="row">
                                                    <div class="col-12">
                                                        <div class="form-group row">
                                                            {!! Form::label('openai_key', 'OpenAl Api Key', ['class' => 'col-xl-2 col-md-3 col-form-label']) !!}
                                                            <div class="col-xl-6 col-md-4">
                                                                {!! Form::text('name[openai_key]',App\Models\Setting::getSetting('openai_key'),['class' => 'form-control','required']) !!}
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            @endif

                                            <div class="row mt-3">
                                                <div class="col-12">
                                                    <div class="form-group row">
                                                        {!! Form::label('openai_model','OpenAI Model', ['class' => 'col-xl-2 col-md-3 col-form-label']) !!}
                                                        <div class="col-xl-6 col-md-9">
                                                            <?php $model = App\Models\OpenaiModel::get(); ?>
                                                            <select id="openai_model" name="name[openai_model]" class="form-control" required>
                                                                @foreach($model as $m)
                                                                <option value="{{$m->name}}" @if(App\Models\Setting::getSetting('openai_model') == $m->name) selected @endif>{{$m->name}}</option>
                                                                @endforeach
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="row mb-4">
                                                <div class="col-md-12 text-center">
                                                @if(Auth::user()->user_type == "Demo")
                                                <button type="button" class="btn btn-success ToastrButton mb-3 mb-md-0">Save</button>
                                                @else
                                                {!! Form::submit('Save', ['class' => 'btn btn-success mb-3 mb-md-0']) !!}
                                                @endif
                                                </div>
                                            </div>
                                            {!! Form::close() !!}

                                            {!! Form::open(['url' =>'admin/add-openai-model','method'=>'POST','id'=>'model-form']) !!}
                                            <div class="row">
                                                <div class="col-12">
                                                    <div class="form-group row">
                                                        {!! Form::label('model_name', 'Add New Model', ['class' => 'col-xl-2 col-md-3 col-form-label']) !!}
                                                        <div class="col-xl-6 col-md-4">
                                                            {!! Form::text('model_name',null,['class' => 'form-control','id'=>'model_name','required']) !!}
                                                        </div>
                                                    </div>
                                                    <div class="error-msg"></div>
                                                </div>
                                            </div>

                                            <div class="row">
                                                <div class="col-md-12 text-center">
                                                @if(Auth::user()->user_type == "Demo")
                                                <button type="button" class="btn btn-success ToastrButton mb-3 mb-md-0">Save</button>
                                                @else
                                                <button type="button" class="btn btn-success mb-3 mb-md-0 addBtn">Save</button>
                                                @endif
                                                </div>
                                            </div>
                                            {!! Form::close() !!}
                                            </div>
                                        </div>
                                    </div>

                                    <div class="vertab-content">
                                        <div class="panel-heading">
                                            <h4 class="panel-title" data-toggle="collapse" data-parent="#accordion" data-target="#collapse55">
                                                Payment Setting
                                            </h4>
                                        </div>
                                        <div id="collapse55" class="panel-collapse collapse">
                                            <div class="panel-body">
                                                {!! Form::open(['url' =>'admin/payment-setting','method'=>'POST','files'=>true]) !!}

                                                <div class="row">
                                                    <div class="col-12">
                                                        <div class="form-group row">
                                                            {!! Form::label('currency','Currency', ['class' => 'col-xl-2 col-md-3 col-3 col-form-label']) !!}
                                                            <div class="col-xl-10 col-md-9 col-9">
                                                                <select class="form-control" id="currency" name="name[currency]" required>
                                                                    <option value="INR" @if(App\Models\PaymentSetting::getPaymentSetting('currency') == "INR") selected @endif>INR</option>
                                                                    <option value="USD" @if(App\Models\PaymentSetting::getPaymentSetting('currency') == "USD") selected @endif>USD</option>
                                                                </select>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <div class="card card-primary">
                                                            <div class="card-header">
                                                                <span>Razorpay</span>
                                                                <label class="cl-switch cl-switch-blue float-right">
                                                                    <input type="checkbox" id="razorpay_enable" value="1" name="name[razorpay_enable]" @if(App\Models\PaymentSetting::getPaymentSetting('razorpay_enable')==1) checked @endif>
                                                                    <span class="switcher"></span>
                                                                </label>
                                                            </div>
                                                            <div class="card-body">
                                                                <div class="form-group">
                                                                    <label for="razorpay_key_id">Razorpay Key Id</label>
                                                                    <input type="text" class="form-control" name="name[razorpay_key_id]" id="razorpay_key_id" value="{{App\Models\PaymentSetting::getPaymentSetting('razorpay_key_id')}}" placeholder="Enter Razorpay Key Id">
                                                                </div>
                                                                <div class="form-group">
                                                                    <label for="razorpay_key_secret">Razorpay Secret Key</label>
                                                                    <input type="text" class="form-control" name="name[razorpay_key_secret]" id="razorpay_key_secret" value="{{App\Models\PaymentSetting::getPaymentSetting('razorpay_key_secret')}}" placeholder="Enter Razorpay Secret Key">
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="card card-primary">
                                                            <div class="card-header">
                                                                <span>Stripe</span>
                                                                <label class="cl-switch cl-switch-blue float-right">
                                                                    <input type="checkbox" id="stripe_enable" value="1" name="name[stripe_enable]" @if(App\Models\PaymentSetting::getPaymentSetting('stripe_enable')==1) checked @endif>
                                                                    <span class="switcher"></span>
                                                                </label>
                                                            </div>
                                                            <div class="card-body">
                                                                <div class="form-group">
                                                                    <label for="stripe_publishable_Key">Stripe Publishable Key</label>
                                                                    <input type="text" class="form-control" name="name[stripe_publishable_Key]" id="stripe_publishable_Key" value="{{App\Models\PaymentSetting::getPaymentSetting('stripe_publishable_Key')}}" placeholder="Enter Stripe Publishable Key">
                                                                </div>
                                                                <div class="form-group">
                                                                    <label for="stripe_secret_key">Stripe Secret Key</label>
                                                                    <textarea rows="3" class="form-control" name="name[stripe_secret_key]" id="stripe_secret_key">{{App\Models\PaymentSetting::getPaymentSetting('stripe_secret_key')}}</textarea>
                                                                </div>
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
                                    </div>

                                    <div class="vertab-content">
                                        <div class="panel-heading">
                                            <h4 class="panel-title" data-toggle="collapse" data-parent="#accordion" data-target="#collapse2">
                                                Email Setting
                                            </h4>
                                        </div>
                                        <div id="collapse2" class="panel-collapse collapse">
                                            <div class="panel-body">
                                            {!! Form::open(['url' =>'admin/email-setting','method'=>'POST','files'=>true]) !!}

                                            <div class="row mt-3">
                                                <div class="col-12">
                                                    <div class="form-group row">
                                                        {!! Form::label('smtp_host','SMTP Host', ['class' => 'col-xl-2 col-md-3 col-form-label']) !!}
                                                        <div class="col-xl-5 col-md-9">
                                                            {!! Form::text('name[smtp_host]',App\Models\EmailSetting::getEmailSetting('smtp_host'),['class' => 'form-control','required']) !!}
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="row mt-3">
                                                <div class="col-12">
                                                    <div class="form-group row">
                                                        {!! Form::label('username','Username', ['class' => 'col-xl-2 col-md-3 col-form-label']) !!}
                                                        <div class="col-xl-5 col-md-9">
                                                            {!! Form::text('name[username]',App\Models\EmailSetting::getEmailSetting('username'),['class' => 'form-control','required']) !!}
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="row mt-3">
                                                <div class="col-12">
                                                    <div class="form-group row">
                                                        {!! Form::label('password','Password', ['class' => 'col-xl-2 col-md-3 col-form-label']) !!}
                                                        <div class="col-xl-5 col-md-9">
                                                            {!! Form::text('name[password]',null,['class' => 'form-control']) !!}
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="row mt-3">
                                                <div class="col-12">
                                                    <div class="form-group row">
                                                        {!! Form::label('password','SMTP Secure', ['class' => 'col-xl-2 col-md-3 col-form-label']) !!}
                                                        <div class="col-xl-5 col-md-9">
                                                            <div class="row">
                                                                <div class="col-6">
                                                                    <select id="encryption" name="name[encryption]" class="form-control" required>
                                                                        <option value="ssl" @if(App\Models\EmailSetting::getEmailSetting('encryption') == "ssl") selected @endif>SSL</option>
                                                                        <option value="tls" @if(App\Models\EmailSetting::getEmailSetting('encryption') == "tls") selected @endif>TLS</option>
                                                                    </select>
                                                                </div>
                                                                <div class="col-6">
                                                                    {!! Form::number('name[port]',App\Models\EmailSetting::getEmailSetting('port'),['class' => 'form-control','required']) !!}
                                                                </div>
                                                            </div>
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

                                    <div class="vertab-content">
                                        <div class="panel-heading">
                                            <h4 class="panel-title" data-toggle="collapse" data-parent="#accordion" data-target="#collapse4">
                                                Invoice Setting
                                            </h4>
                                        </div>
                                        <div id="collapse4" class="panel-collapse collapse">
                                            <div class="panel-body">
                                                {!! Form::open(['url' =>'admin/invoice-setting','method'=>'POST','files'=>true]) !!}
                                            
                                                <div class="row">
                                                    <div class="col-12">
                                                        <div class="form-group row">
                                                            {!! Form::label('invoice_logo','Invoice Logo', ['class' => 'col-xl-2 col-md-3 col-form-label']) !!}
                                                            <div class="col-xl-10 col-md-9">
                                                                <div class="row">
                                                                    <div class="col-xl-3"><input class="form-control" type="file" id="invoice_logo" name="name[invoice_logo]"></div>
                                                                    <div class="col-xl-9" id="preview3"><img type="image" class="shadow bg-white rounded" src="@if(App\Models\InvoiceSetting::getInvoiceSetting('invoice_logo')){{asset('uploads/'.App\Models\InvoiceSetting::getInvoiceSetting('invoice_logo'))}}@else{{asset('assets/images/no-image.png')}}@endif" alt="Image" style="width: 100px;height: 100px" /></div>  
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-md-4">
                                                        <div class="form-group">
                                                            {!! Form::label('invoice_prefix', 'Invoice Prefix', ['class' => 'col-form-label']) !!}
                                                            {!! Form::text('name[invoice_prefix]',App\Models\InvoiceSetting::getInvoiceSetting('invoice_prefix'),['class' => 'form-control','id'=>'invoice_prefix','required']) !!}
                                                        </div>
                                                    </div>
                                                    <div class="col-md-4">
                                                        <div class="form-group">
                                                            {!! Form::label('invoice_number_digit', 'Invoice Nnumber Digits', ['class' => 'col-form-label']) !!}
                                                            {!! Form::number('name[invoice_number_digit]',App\Models\InvoiceSetting::getInvoiceSetting('invoice_number_digit'),['class' => 'form-control','id'=>'invoice_number_digit','required']) !!}
                                                        </div>
                                                    </div>
                                                    <div class="col-md-4">
                                                        <div class="form-group">
                                                            <label for="invoice_number_example" class="col-form-label">Invoice Number Example</label>
                                                            <input type="text" class="form-control" id="invoice_number_example" readonly>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-12">
                                                        <div class="form-group row">
                                                            {!! Form::label('terms_and_conditions', 'Terms and Conditions', ['class' => 'col-form-label']) !!}
                                                            {!! Form::textarea('name[terms_and_conditions]',App\Models\InvoiceSetting::getInvoiceSetting('terms_and_conditions'),['class' => 'form-control',"rows"=>3,'required']) !!}
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

                                    <div class="vertab-content">
                                        <div class="panel-heading">
                                            <h4 class="panel-title" data-toggle="collapse" data-parent="#accordion" data-target="#collapse8">
                                                App Update Popup
                                            </h4>
                                        </div>
                                        <div id="collapse8" class="panel-collapse collapse">
                                            <div class="panel-body">
                                                {!! Form::open(['url' =>'admin/app-update-setting','method'=>'POST','files'=>true]) !!}

                                                <div class="row mt-3">
                                                    <div class="col-12">
                                                        <div class="form-group row">
                                                            {!! Form::label('update_popup_show','App Update Popup Show/Hide', ['class' => 'col-xl-3 col-md-4 col-form-label']) !!}
                                                            <div class="col-xl-5 col-md-8">
                                                                <label class="cl-switch cl-switch-blue">
                                                                    <input type="checkbox" id="update_popup_show" value="1" name="name[update_popup_show]" @if(App\Models\AppUpdateSetting::getAppUpdateSetting('update_popup_show')==1) checked @endif>
                                                                    <span class="switcher"></span>
                                                                </label>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row mt-3">
                                                    <div class="col-12">
                                                        <div class="form-group row">
                                                            {!! Form::label('new_app_version_code','New App Version Code', ['class' => 'col-xl-3 col-md-4 col-form-label']) !!}
                                                            <div class="col-xl-5 col-md-8">
                                                                {!! Form::text('name[new_app_version_code]',App\Models\AppUpdateSetting::getAppUpdateSetting('new_app_version_code'),['class' => 'form-control','required']) !!}
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row mt-3">
                                                    <div class="col-12">
                                                        <div class="form-group row">
                                                            {!! Form::label('description','Description', ['class' => 'col-xl-3 col-md-4 col-form-label']) !!}
                                                            <div class="col-xl-5 col-md-8">
                                                                {!! Form::textarea('name[description]',App\Models\AppUpdateSetting::getAppUpdateSetting('description'),['class' => 'form-control','rows'=>7,'required']) !!}
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row mt-3">
                                                    <div class="col-12">
                                                        <div class="form-group row">
                                                            {!! Form::label('app_link','App Link', ['class' => 'col-xl-3 col-md-4 col-form-label']) !!}
                                                            <div class="col-xl-5 col-md-8">
                                                                {!! Form::text('name[app_link]',App\Models\AppUpdateSetting::getAppUpdateSetting('app_link'),['class' => 'form-control','required']) !!}
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row mt-3">
                                                    <div class="col-12">
                                                        <div class="form-group row">
                                                            {!! Form::label('cancel_option','Cancel Option', ['class' => 'col-xl-3 col-md-4 col-form-label']) !!}
                                                            <div class="col-xl-5 col-md-8">
                                                                <label class="cl-switch cl-switch-blue">
                                                                    <input type="checkbox" id="cancel_option" value="1" name="name[cancel_option]" @if(App\Models\AppUpdateSetting::getAppUpdateSetting('cancel_option')==1) checked @endif>
                                                                    <span class="switcher"></span>
                                                                </label>
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

                                    <div class="vertab-content">
                                        <div class="panel-heading">
                                            <h4 class="panel-title" data-toggle="collapse" data-parent="#accordion" data-target="#collapse5">
                                                About Us
                                            </h4>
                                        </div>
                                        <div id="collapse5" class="panel-collapse collapse">
                                            <div class="panel-body">
                                                {!! Form::open(['url' =>'admin/other-setting','method'=>'POST','files'=>true]) !!}

                                                <div class="row">
                                                    <div class="col-12">
                                                        <div class="form-group row">
                                                            {!! Form::label('about_us','About Us', ['class' => 'col-md-2 col-form-label']) !!}
                                                            <div class="col-md-10">
                                                                <textarea name="name[about_us]" id="about_us" class="form-control" required>{{App\Models\OtherSetting::getOtherSetting('about_us')}}</textarea>
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

                                    <div class="vertab-content">
                                        <div class="panel-heading">
                                            <h4 class="panel-title" data-toggle="collapse" data-parent="#accordion" data-target="#collapse5A">
                                                Privacy Policy
                                            </h4>
                                        </div>
                                        <div id="collapse5A" class="panel-collapse collapse">
                                            <div class="panel-body">
                                                {!! Form::open(['url' =>'admin/other-setting','method'=>'POST','files'=>true]) !!}

                                                <div class="row">
                                                    <div class="col-12">
                                                        <div class="form-group row">
                                                            {!! Form::label('privacy_policy','Privacy Policy', ['class' => 'col-md-2 col-form-label']) !!}
                                                            <div class="col-md-10">
                                                                <textarea name="name[privacy_policy]" id="privacy_policy" class="form-control" required>{{App\Models\OtherSetting::getOtherSetting('privacy_policy')}}</textarea>
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

                                    <div class="vertab-content">
                                        <div class="panel-heading">
                                            <h4 class="panel-title" data-toggle="collapse" data-parent="#accordion" data-target="#collapse6">
                                                Refund Policy
                                            </h4>
                                        </div>
                                        <div id="collapse6" class="panel-collapse collapse">
                                            <div class="panel-body">
                                                {!! Form::open(['url' =>'admin/other-setting','method'=>'POST','files'=>true]) !!}

                                                <div class="row">
                                                    <div class="col-12">
                                                        <div class="form-group row">
                                                            {!! Form::label('refund_policy','Refund Policy', ['class' => 'col-md-2 col-form-label']) !!}
                                                            <div class="col-md-10">
                                                                <textarea name="name[refund_policy]" id="refund_policy" class="form-control" required>{{App\Models\OtherSetting::getOtherSetting('refund_policy')}}</textarea>
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

                                    <div class="vertab-content">
                                        <div class="panel-heading">
                                            <h4 class="panel-title" data-toggle="collapse" data-parent="#accordion" data-target="#collapse7">
                                                Terms & Condition
                                            </h4>
                                        </div>
                                        <div id="collapse7" class="panel-collapse collapse">
                                            <div class="panel-body">
                                                {!! Form::open(['url' =>'admin/other-setting','method'=>'POST','files'=>true]) !!}

                                                <div class="row">
                                                    <div class="col-12">
                                                        <div class="form-group row">
                                                            {!! Form::label('terms_condition','Terms & Condition', ['class' => 'col-md-2 col-form-label']) !!}
                                                            <div class="col-md-10">
                                                                <textarea name="name[terms_condition]" id="terms_condition" class="form-control" required>{{App\Models\OtherSetting::getOtherSetting('terms_condition')}}</textarea>
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
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
@endsection

@section("script")
<script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.11/summernote-lite.js"></script>
<script type="text/javascript">
    // $('#rewarded_display_type').select2();
    // $('#banner_display_type').select2();
    // $('#interstitial_display_type').select2();
    // $('#native_display_type').select2();
    $('#currency').select2();
    $('#openai_model').select2();
    $('#encryption').select2();
    $('#default_currency').select2();

    $('#about_us').summernote({
        placeholder: '',
        tabsize: 2,
        height: 400
    });

    $('#privacy_policy').summernote({
        placeholder: '',
        tabsize: 2,
        height: 400
    });

    $('#refund_policy').summernote({
        placeholder: '',
        tabsize: 2,
        height: 400
    });

    $('#terms_condition').summernote({
        placeholder: '',
        tabsize: 2,
        height: 400
    });

    $('#bank_detail').summernote({
        placeholder: '',
        tabsize: 2,
        height: 200
    });

    $('#footer_text').summernote({
        placeholder: '',
        tabsize: 2,
        height: 200
    });

    $(".addBtn").click(function(){
      var name = $('#model_name').val();
     
      $.ajax({
        type: "POST",
        url: "{{url('admin/check-model-available')}}",
        data: {name : name},
        headers: { 'X-CSRF-TOKEN': $('meta[name="csrf-token"]').attr('content') },
        success: function(data) {
          if(data == 1)
          {
            $("#model-form").submit();
          }
          if(data == 0)
          {
            $('.error-msg').empty();
            $('.error-msg').append('<div class="alert alert-danger"><span>This model not available this API key</span></div>');
          }
        },
      });
    });

    function imagePreview(fileInput) 
    { 
        if (fileInput.files && fileInput.files[0]) 
        {
            var fileReader = new FileReader();
            fileReader.onload = function (event) 
            {
                $('#preview').html('<img src="'+event.target.result+'" class="shadow bg-white rounded" width="200px" alt="Select Image" height="100px"/>');
            };
            fileReader.readAsDataURL(fileInput.files[0]);
        }
    }

    $("#black_logo").change(function () {
        imagePreview(this);
    });

    function imagePreview1(fileInput) 
    { 
        if (fileInput.files && fileInput.files[0]) 
        {
            var fileReader = new FileReader();
            fileReader.onload = function (event) 
            {
                $('#preview1').html('<img src="'+event.target.result+'" class="shadow bg-white rounded" width="200px" alt="Select Image" height="100px"/>');
            };
            fileReader.readAsDataURL(fileInput.files[0]);
        }
    }

    $("#white_logo").change(function () {
        imagePreview1(this);
    });

    function imagePreview2(fileInput) 
    { 
        if (fileInput.files && fileInput.files[0]) 
        {
            var fileReader = new FileReader();
            fileReader.onload = function (event) 
            {
                $('#preview2').html('<img src="'+event.target.result+'" class="shadow bg-white rounded" width="50px" alt="Select Image" height="50px"/>');
            };
            fileReader.readAsDataURL(fileInput.files[0]);
        }
    }

    $("#favicon").change(function () {
        imagePreview2(this);
    });

    function imagePreview3(fileInput) 
    { 
        if (fileInput.files && fileInput.files[0]) 
        {
            var fileReader = new FileReader();
            fileReader.onload = function (event) 
            {
                $('#preview3').html('<img src="'+event.target.result+'" class="shadow bg-white rounded" width="100px" alt="Select Image" height="100px"/>');
            };
            fileReader.readAsDataURL(fileInput.files[0]);
        }
    }

    $("#invoice_logo").change(function () {
        imagePreview3(this);
    });

    $("#invoice_prefix").keyup(function() {
        var invoice_prefix = $("#invoice_prefix").val();
        var invoice_number_digit = $("#invoice_number_digit").val();

        $("#invoice_number_example").val(invoice_prefix+'#'+('0'.repeat(invoice_number_digit) + 1).slice(-invoice_number_digit));
    });

    $("#invoice_number_digit").keyup(function() {
        var invoice_prefix = $("#invoice_prefix").val();
        var invoice_number_digit = $("#invoice_number_digit").val();

        $("#invoice_number_example").val(invoice_prefix+'#'+('0'.repeat(invoice_number_digit) + 1).slice(-invoice_number_digit));
    });

    $(document).ready(function() {
        var invoice_prefix = $("#invoice_prefix").val();
        var invoice_number_digit = $("#invoice_number_digit").val();

        $("#invoice_number_example").val(invoice_prefix+'#'+('0'.repeat(invoice_number_digit) + 1).slice(-invoice_number_digit));
    });
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
	jQuery( window ).on( "resize orientationchange",function() 
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
	resize_vertical_accordions();
});

function resize_vertical_accordions() 
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