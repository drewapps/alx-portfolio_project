@extends('layouts.frontend')

@section('css')
<style>
.ring-1, .ring-2 {
    --tw-shadow: 0 1px 2px 0 rgba(0,0,0,.05);
    box-shadow: var(--tw-ring-offset-shadow),var(--tw-ring-shadow),var(--tw-shadow,0 0 #0000) !important;
}
.ring-gray-200 {
    --tw-ring-color: rgb(226 232 240/ 1);
}
.ring-1 {
    --tw-ring-offset-shadow: var(--tw-ring-inset) 0 0 0 0 #fff;
    --tw-ring-shadow: var(--tw-ring-inset) 0 0 0 calc(1px + 0) rgb(226 232 240/ 1);
}
.p-6 {
    padding: 1.5rem;
}
.rounded-2xl {
    border-radius: 1rem;
}
.flex {
    display: flex;
}
.flex-col {
    flex-direction: column;
}
.bg-white {
    background-color: #fff!important;
}
.bg-gray-100\/75 {
    background-color: rgba(241,245,249,.75);
}
.justify-center {
    justify-content: center;
}
.items-center {
    align-items: center;
}
.w-16 {
    width: 4rem;
}
.h-16 {
    height: 4rem;
}
.mb-6 {
    margin-bottom: 1.5rem;
}
.font-semibold {
    font-weight: 800;
}
.text-gray-700 {
    --tw-text-opacity: 1;
    color: rgb(51 65 85/1);
}
.text-lg {
    font-size: 1.25rem;
    line-height: 1.75rem;
}
.rounded-full {
    border-radius: 9999px;
}
.h-full {
    height: 100%;
}
@media (min-width: 1280px)
{
    .xl\:grid-cols-4 {
        grid-template-columns: repeat(4,minmax(0,1fr));
    }
}
@media (min-width: 1024px)
{
    .lg\:grid-cols-3 {
        grid-template-columns: repeat(3,minmax(0,1fr));
    }
}
.gap-5 {
    gap: 1.25rem;
}
.grid {
    display: grid;
}
.my-style{
    border: 1px solid #cbd5e1;
    box-shadow: 0 10px 15px -3px rgba(0,0,0,.1),0 4px 6px -4px rgba(0,0,0,.1) !important;
}
.my-style:hover{
    border: 2px solid #cbd5e1;
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
  min-width: 20px;
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
.pill-most-popular{
    background-color: #fc5185;
    border: none;
    color: #fefbfd;
    padding: 10px 30px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    margin: 2px 2px;
    cursor: pointer;
    border-radius: 30px;
}
@media (min-width: 1200px) {
    .poster-img{
        width:600px;
        margin-top:50px;
    }
}
@media (max-width: 576px) {
    .poster-img{
        width:400px;
    }
}
</style>
@endsection

@section('content')
    <div class="">
        <div class="container-fluid section w-form text-center py-3 pt-md-5 pt-0" style="background-color:#2c2c2c;">
            <div class="container jk-in-top2-image">
                <div class="jk-in-top2-img1">
                    <img src="{{asset('assets/frontend/images/all/pi_1.png')}}" alt="" width="70" style="margin: 0px 0px 10px 15px;"><br>
                    <p class="jk-in-top2-img1-text" style="background-color:#66557a99;"><b>Freelancer</b></p>
                </div>

                <div class="jk-in-top2-img2">
                    <img src="{{asset('assets/frontend/images/all/pi_2.png')}}" alt="" width="70" style="margin: 0px 0px 10px 15px;"><br>
                    <p class="jk-in-top2-img1-text" style="background-color:#00bbe64f;"><b>Blogger</b></p>
                </div>

                <div class="jk-in-top2-img3">
                    <img src="{{asset('assets/frontend/images/all/pi_3.png')}}" alt="" width="70" style="margin: 0px 0px 10px 15px;"><br>
                    <p class="jk-in-top2-img1-text" style="background-color:#94a8c13d;"><b>Developer</b></p>
                </div>

                <div class="jk-in-top2-img4">
                    <img src="{{asset('assets/frontend/images/all/pi_4.png')}}" alt="" width="70" style="margin: 0px 0px 10px 15px;"><br>
                    <p class="jk-in-top2-img1-text" style="background-color:#060f168f;"><b>Marketer</b></p>
                </div>
            </div>

            <h1 class="jk-in-top2-maintext"><b style="font-weight: 800;">Best AI Writer for Creating</b></h1>
            <div style="display: flex;">
                <h1 class="jk-in-top2-animation" style="font-weight: 800">
                    <a href="" class="typewrite" data-period="2000"
                        data-type='[ "Linkedin Post.", "Blog Articles.", "SEO Content.", "Product Descriptions.", "Facebook Ads.", "Google Ads.", "Twitter Twites.", "Instagram Captions." ]'>
                    </a>
                    <span class="wrap"></span>
                </h1>
            </div>
            <p class="jk-in-top2-text m-0 mt-md-5" style="text-align:center;">Create SEO-optimized and plagiarism-free content <br>for your blogs, ads, emails, and
                website 10X faster.</p> <br>
            <a href="{{url('login')}}" class="jk-in-top2-button btn btn-lg mb-4 wow fadeInUp" data-wow-delay="0.1s" style="background-color:#FAE05F;color:#212529;"><b>Start Writing For Free</b></a>
            <div class="mb-md-5 mb-0" style="color: hsla(0,0%,100%,.5); font-size:16px; text-align:center;">No credit card required.</div>
        </div>

        <div class="container-fluid section customer-logos wf-section">
            <div class="container w-container">
                <h5 class="dark-text">Trusted by 100,000+ Teams Globally at Innovative companies including...</h5>
                <div class="row">
                    @foreach ($company as $key=>$comp)
                        @if($key % 3 == 0 || $key % 3 == 1 || $key % 3 == 2)
                        <div class="col-lg-3 col-md-4 col-6">
                        <img src="{{asset('uploads/company/'.$comp->image)}}" loading="lazy" class="mt-3" width="161" height="50" alt="img" class="logo-s"/>
                        </div>
                        @endif
                    @endforeach
                </div>
            </div>
        </div>
        <div class="divider-line"></div>

        <div class="show-nav-container">
            <div class="container py-1 py-md-5 my-1 my-md-3">
                <div class="row">
                    <div class="col-10 offset-1 col-sm-12 offset-sm-0 col-md-5 col-lg-6 mb-4 p-0">
                        <h5 class="text-secondary">YOU NAME IT. WE HAVE IT.</h5>
                        <div class="how-scalenut"><b style="font-weight: 700;">How is <br> Magik Writer <span class="gradient-underline">different</span></b></div>
                        <h5 class="text-dark py-4 py-md-4">A super platform to simplify your content need.</h5>
                        <a href="{{url('login')}}" class="btn btn-lg btn-primary mb-5 mb-md-0">Try For Free  <i class="fa-solid fa-arrow-right ml-1"></i></a>
                    </div>
                    <div class="col-sm-6 col-md-3 col-lg-3">
                        <div class="row">
                            <div class="col-2 offset-1 offset-sm-0 col-sm-3 col-md-4 col-lg-3 p-0">
                                <img src="{{asset('assets/frontend/images/all/op_1.svg')}}" alt="" height="50px" width="50px" class="">
                            </div>
                            <div class="col-8 col-sm-9 col-md-8 col-lg-9 p-0 ml-1 ml-sm-0">
                                <span class="text-dark float-left" style="font-size: 18px;font-weight:600;">80+ use cases and templates</span>
                            </div>
                        </div>

                        <div class="row my-4 my-md-5">
                            <div class="col-2 offset-1 offset-sm-0 col-sm-3 col-md-4 col-lg-3 p-0">
                                <img src="{{asset('assets/frontend/images/all/op_3.svg')}}" alt="" height="50px" width="50px" class="">
                            </div>
                            <div class="col-8 col-sm-9 col-md-8 col-lg-9 p-0 ml-1 ml-sm-0">
                                <span class="text-dark float-left" style="font-size:18px;font-weight:600;">Great Optimized for Conversions</span>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-2 offset-1 offset-sm-0 col-sm-3 col-md-4 col-lg-3 p-0">
                                <img src="{{asset('assets/frontend/images/all/op_5.svg')}}" alt="" height="50px" width="50px" class="">
                            </div>
                            <div class="col-8 col-sm-9 col-md-8 col-lg-9 p-0 ml-1 ml-sm-0">
                                <span class="text-dark float-left" style="font-size: 18px;font-weight:600;">Choose from 30+ languages to write own</span>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6 col-md-3 col-lg-3 pt-3 pt-md-0">
                        <div class="row">
                            <div class="col-2 offset-1 offset-sm-0 col-sm-3 col-md-4 col-lg-3 p-0">
                                <img src="{{asset('assets/frontend/images/all/op_2.svg')}}" alt="" height="50px" width="50px" class="">
                            </div>
                            <div class="col-8 col-sm-9 col-md-8 col-lg-9 p-0 ml-1 ml-sm-0">
                                <span class="text-dark float-left" style="font-size: 18px;font-weight:600;">Make SEO Content That Google Loves</span>
                            </div>
                        </div>

                        <div class="row my-4 my-md-5">
                            <div class="col-2 offset-1 offset-sm-0 col-sm-3 col-md-4 col-lg-3 p-0">
                                <img src="{{asset('assets/frontend/images/all/op_4.svg')}}" alt="" height="50px" width="50px" class="">
                            </div>
                            <div class="col-8 col-sm-9 col-md-8 col-lg-9 p-0 ml-1 ml-sm-0">
                                <span class="text-dark float-left" style="font-size: 18px;font-weight:600;">Magik Writer Read Latest Information</span>
                            </div>
                        </div>

                        <div class="row mb-5">
                            <div class="col-2 offset-1 offset-sm-0 col-sm-3 col-md-4 col-lg-3 p-0">
                                <img src="{{asset('assets/frontend/images/all/op_6.svg')}}" alt="" height="50px" width="50px" class="">
                            </div>
                            <div class="col-8 col-sm-9 col-md-8 col-lg-9 p-0 ml-1 ml-sm-0">
                                <span class="text-dark float-left" style="font-size: 18px;font-weight:600;">Extensive formatting options to make text richer</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="container my-md-5 my-3" id="ai-image">
                <h1 class="text-center dark-text" style="font-weight: 700;"><span class="gradient-underline">How to make AI-generated images</span></h1>
                <div class="row">
                    <div class="col-md-6 col-12">
                        <div class="row mt-5">
                            <div class="col-md-1 col-2 mt-1"><span class="rounded-circle bg-primary text-light h5 mt-1 px-2">1</span></div>
                            <div class="col-md-11 col-10">
                                <h4 class="text-dark"><b>Click on "Magik Art" Option</b></h4>
                                <span class="text-justify text-dark" style="text-transform:none;">Go to the sidebar and click "Magik Art". Then, click on the Text to Image Generation area.</span>
                            </div>
                        </div>
                        <div class="row mt-3">
                            <div class="col-md-1 col-2 mt-1"><span class="rounded-circle bg-primary text-light h5 mt-1 px-2">2</span></div>
                            <div class="col-md-11 col-10">
                                <h4 class="text-dark"><b>Convert text to image</b></h4>
                                <span class="text-justify text-dark" style="text-transform:none;">Enter your text prompt on our text-to-image generator to convert it into an image. Get creative and descriptive with your prompt.</span>
                            </div>
                        </div>
                        <div class="row mt-3">
                            <div class="col-md-1 col-2 mt-1"><span class="rounded-circle bg-primary text-light h5 mt-1 px-2">3</span></div>
                            <div class="col-md-11 col-10">
                                <h4 class="text-dark"><b>Enhance your AI image Option</b></h4>
                                <span class="text-justify text-dark" style="text-transform:none;">You can choose any available image style like Photo, Drawing, or Pattern. Or use the style option to get a random style.</span>
                            </div>
                        </div>
                        <div class="row mt-3">
                            <div class="col-md-1 col-2 mt-1"><span class="rounded-circle bg-primary text-light h5 mt-1 px-2">4</span></div>
                            <div class="col-md-11 col-10">
                                <h4 class="text-dark"><b>Explore Your media library</b></h4>
                                <span class="text-justify text-dark" style="text-transform:none;">Your AI-generated images or design project with fun graphic design elements from our free media library.</span>
                            </div>
                        </div>
                        <div class="row mt-3">
                            <div class="col-md-1 col-2 mt-1"><span class="rounded-circle bg-primary text-light h5 mt-1 px-2">5</span></div>
                            <div class="col-md-11 col-10">
                                <h4 class="text-dark"><b>Download and share</b></h4>
                                <span class="text-justify text-dark" style="text-transform:none;">Download your unique AI-generated image in a high-resolution image format. Or save your design project that contains the converted images.</span>
                            </div>
                        </div>
                        <a href="{{url('user/image')}}" class="btn btn-primary text-white btn-lg my-4 mb-md-5 ml-5">Genearte AI Images</a>
                    </div>
                    <div class="col-md-6 col-12 wow fadeInUp" data-wow-delay="0.1s">
                        <img src="{{asset('assets/frontend/images/all/Image_1.png')}}" alt="img" class="responsive poster-img">
                    </div>
                </div>
            </div>

            <div class="section container" id="template" style="padding:30px 4%;">
                <h1 class="text-center pt-1 dark-text" style="font-weight: 700;">AI content writing that creates <span class="gradient-underline mb-1">high-converting content</span></h1>

                <div class="my-2 grid gap-5 mb-12 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 mt-5" id="all_templates">
                    @foreach($templates as $temp)
                    <div id="template-card-{{$temp->id}}" class="wow fadeInUp" data-wow-delay="0.1s">
                        <div class="my-style relative p-6 transition-all h-full focus:ring-gray-400 focus:shadow-xl duration-150 rounded-2xl shadow-sm hover:shadow-lg hover:ring-gray-300 hover:ring-2 ring-1 ring-gray-200 group flex flex-col bg-white">
                            <a href="{{url('user/templates/'.$temp->slug)}}" style="color: #000;text-decoration: none;">
                            <div>
                                <div class="flex items-center justify-center w-16 h-16 text-2xl rounded-full text-gray-600 mb-6 bg-gray-100/75">
                                    <img src="{{asset('assets/images/social/'.$temp->image)}}" width="44" height="44">
                                </div>
                                <h3 class="mb-2 text-lg font-semibold text-gray-700">{{$temp->title}}</h3>
                                <p class="text-gray-500 flex-1">{{$temp->description}}</p>
                            </div>
                            </a>
                        </div>
                    </div>
                    @endforeach
                </div>
                <div class="row">
                    <div class="col-12 text-center mt-4"><a class="btn btn-primary" href="{{url('templates')}}" role="button">View More</a></div>
                </div>
            </div>

            <div class="section zero-overflow wf-section" id="benefit" style="padding:25px 4%;">
                <div class="container w-container">
                    <div class="heading-wrapper _800px-max _80px-bottom-margin">
                        <span class="chapter-title" style="font-size:13px;font-weight: 700;">Benefits of Magik Writer</span>
                        <h1 class="dark-text mt-3" style="font-weight: 700;">How will Magik Writer unlock your <span class="gradient-underline">creative potential?</span></h1>
                    </div>
                    <div class="benefits-grid">
                        @foreach ($benefit as $bene)
                            <div id="w-node-_5771f27c-4d2c-165c-1674-deedc3c0583f-c3c0583f" class="benefits-block">
                                <div class="div-block-3200">
                                    <div class="row">
                                        <div class="col-2 col-lg-1 offset-lg-1 mt-2 p-0"><img src="{{asset('uploads/'.$bene->image)}}" height="40px" width="auto" alt="img"></div>
                                        <div class="col-10">
                                            <b class="benefits-headline">{{$bene->title}}</b>
                                            <div class="mt-2" style="font-size:19px;">{{$bene->description}}</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        @endforeach
                    </div>
                </div>
            </div>
        </div>
        
        <div class="section" id="subscription" style="padding:20px 4%;"> 
            <div class="container w-container">
                <div class="heading-wrapper _800px-max">
                    <h1 class="dark-text" style="font-weight: 700;">Subscribe Now!</h1>
                </div>
                <div class="row">
                    <div class="col-8 offset-2 mt-3" style="text-align: center;">
                        <span> Monthly </span>
                        <label class="switch" style="margin-bottom:-0.5rem;">
                            <input type="checkbox" id="plan_type" class="status" @if($plan_data == 'yearly') checked @endif>
                            <span class="slider round"></span> 
                        </label>
                        <span> Yearly </span>
                    </div>
                    {!! Form::open(['url' => 'change-plan-type#subscription','method'=>'POST','class'=>'form-horizontal','id'=>'plan_form']) !!}
                    <input type="hidden" name="type" id="type">
                    {!! Form::close() !!}
                </div>
                <div class="row d-flex justify-content-center mt-3">
                    @foreach($subscription as $s)
                    <div class="col-lg-4 col-md-4 col-12 mt-3">
                        <div class="card border border-primary">
                            @if($s->most_popular == 1)<div class="text-center" style="position:absolute; left:0;right:0;top:-20px;"><button class="pill-most-popular"><b>Most Popular</b></button></div>@endif
                            <div class="card-body">
                                <div class="text-center mt-2">
                                    <img src="{{asset('uploads/'.$s->image)}}" alt="trophy" class="mx-auto" style="width:100px !important;height:100px !important;">
                                    <h3 class="text-dark mt-3"><b>{{$s->plan_name}}</b></h3>
                                    <span>{!! $s->description !!}</span>
                                   
                                    <h2 class="mb-4 mt-2 text-primary"><b><span style="font-size:21px;">{{App\Models\PaymentSetting::getPaymentSetting('default_currency')}}</span> {{$s->discount_price}}<span style="font-size:20px;">/{{$s->duration_type}}</span></b></h2>

                                    <span class="text-dark">{{$s->total_words}} Words</span>
                                    <span class="mt-2 text-dark">{{$s->number_of_images}} Images</span>
                                </div>
                                
                                <div class="row mt-4">
                                    <div class="col-12">
                                        <form action="{{url('checkout')}}" method="POST"> 
                                            @csrf
                                            <input type="hidden" name="subscription_id" value="{{$s->id}}">
                                            <button type="submit" class="btn btn-success" style="width:100%;"><b>Subscription</b></button>
                                        </form>
                                    </div>
                                </div>

                                @if(!empty(unserialize($s->include_plan_detail)))
                                <div class="row mt-4">
                                    <div class="col-10 offset-1">
                                        <ul style="line-height:2;">
                                        @foreach(unserialize($s->include_plan_detail) as $detail)
                                            <li><i class="fas fa-check fa-xl text-success"></i> {{$detail}}</li>
                                        @endforeach
                                        </ul> 
                                    </div>
                                </div>
                                @endif

                                @if(!empty(unserialize($s->not_include_plan_detail)))
                                <div class="row">
                                    <div class="col-10 offset-1">
                                        <ul style="line-height:2;margin-left:5px;">
                                        @foreach(unserialize($s->not_include_plan_detail) as $detail1)
                                            <li><i class="fas fa-times fa-xl text-danger" style="margin-right:5px;"></i> {{$detail1}}</li>
                                        @endforeach
                                        </ul> 
                                    </div>
                                </div>
                                @endif
                            </div>
                        </div>
                    </div>
                    @endforeach
                </div>
            </div>
        </div>

        <div class="container my-md-5 my-3" id="review">
            <h1 class="text-dark text-center text-bold mb-md-4 dark-text" style="font-weight: 700;"><span class="gradient-underline">Customers have consistently rated</span></h1>

            <div class="row">
                <div class="col-12 col-sm-6 col-lg-3 px-2 wow fadeInUp" data-wow-delay="0.1s">
                    @foreach ($reviews as $key=>$review)

                        @if($key % 4 == 0)
                            <div class="card shadow-lg mt-4" style="border-radius:20px; @if($key % 5 == 2) background-image:linear-gradient(to left top, rgb(123,54,211), rgb(21,143,210)); color:#FFFFFF;  @elseif($key % 5 == 4) background-image: linear-gradient(to right, rgb(47,56,70), rgb(4,5,8)); color:#FFFFFF; @endif">
                                <div class="card-body">
                                    <p class="card-text" style="font-size:17px;font-style: italic;font-weight: 500;color: gray; @if($key % 5 == 2) color:#FFFFFF;  @elseif($key % 5 == 4) color:#FFFFFF; @endif">"{{$review->quote}}"</p>
                                    <p class="card-text float-left" style="font-size:17px;"><b style="color:black;font-weight:800; @if($key % 5 == 2) color:#FFFFFF;  @elseif($key % 5 == 4) color:#FFFFFF; @endif">{{$review->name}}</b> <br>{{$review->designation}}</p>
                                </div>
                            </div>
                        @endif
                    @endforeach
                </div>

                <div class="col-12 col-sm-6 col-lg-3 px-2 wow fadeInUp" data-wow-delay="0.1s">
                    @foreach ($reviews as $key=>$review)
                        @if($key % 4 == 1)
                            <div class="card shadow-lg mt-4" style="border-radius:20px; @if($key % 5 == 2) background-image:linear-gradient(to left top, rgb(123,54,211), rgb(21,143,210)); color:#FFFFFF;  @elseif($key % 5 == 4) background-image: linear-gradient(to right, rgb(47,56,70), rgb(4,5,8)); color:#FFFFFF; @endif">
                                <div class="card-body">
                                    <p class="card-text" style="font-size:17px;font-style: italic;font-weight: 500;color: gray; @if($key % 5 == 2) color:#FFFFFF;  @elseif($key % 5 == 4) color:#FFFFFF; @endif">"{{$review->quote}}"</p>
                                    <p class="card-text float-left" style="font-size:17px;"><b style="color:black;font-weight:800; @if($key % 5 == 2) color:#FFFFFF;  @elseif($key % 5 == 4) color:#FFFFFF; @endif">{{$review->name}}</b> <br>{{$review->designation}}</p>
                                </div>
                            </div>
                        @endif
                    @endforeach
                </div>

                <div class="col-12 col-sm-6 col-lg-3 px-2 wow fadeInUp" data-wow-delay="0.1s">
                    @foreach ($reviews as $key=>$review)
                        @if($key % 4 == 2)
                            <div class="card shadow-lg mt-4" style="border-radius:20px; @if($key % 5 == 2) background-image:linear-gradient(to left top, rgb(123,54,211), rgb(21,143,210)); color:#FFFFFF;  @elseif($key % 5 == 4) background-image: linear-gradient(to right, rgb(47,56,70), rgb(4,5,8)); color:#FFFFFF; @endif">
                                <div class="card-body">
                                    <p class="card-text" style="font-size:17px;font-style: italic;font-weight: 500;color: gray; @if($key % 5 == 2) color:#FFFFFF;  @elseif($key % 5 == 4) color:#FFFFFF; @endif">"{{$review->quote}}"</p>
                                    <p class="card-text float-left" style="font-size:17px;"><b style="color:black;font-weight:800; @if($key % 5 == 2) color:#FFFFFF;  @elseif($key % 5 == 4) color:#FFFFFF; @endif">{{$review->name}}</b> <br>{{$review->designation}}</p>
                                </div>
                            </div>
                        @endif
                    @endforeach
                </div>

                <div class="col-12 col-sm-6 col-lg-3 px-2 wow fadeInUp" data-wow-delay="0.1s">
                    @foreach ($reviews as $key=>$review)
                        @if($key % 4 == 3)
                            <div class="card shadow-lg mt-4" style="border-radius:20px; @if($key % 5 == 2) background-image:linear-gradient(to left top, rgb(123,54,211), rgb(21,143,210)); color:#FFFFFF;  @elseif($key % 5 == 4) background-image: linear-gradient(to right, rgb(47,56,70), rgb(4,5,8)); color:#FFFFFF; @endif">
                                <div class="card-body">
                                    <p class="card-text" style="font-size:17px;font-style: italic;font-weight: 500;color: gray; @if($key % 5 == 2) color:#FFFFFF;  @elseif($key % 5 == 4) color:#FFFFFF; @endif">"{{$review->quote}}"</p>
                                    <p class="card-text float-left" style="font-size:17px;"><b style="color:black;font-weight:800; @if($key % 5 == 2) color:#FFFFFF;  @elseif($key % 5 == 4) color:#FFFFFF; @endif">{{$review->name}}</b> <br>{{$review->designation}}</p>
                                </div>
                            </div>
                        @endif
                    @endforeach
                </div>
            </div>

            <div class="row text-center my-4 my-md-5">
                <a href="{{url('/login')}}" class="btn btn-lg btn-primary">Start For Free Today <i class="fa-solid fa-arrow-right ml-1"></i></a>
            </div>

        </div>

        <div class="container my-5" id="faq">
            <h1 class="text-center mb-4 mb-md-5 dark-text"><b>Frequently Asked Questions</b></h1>

            <div class="">
                @foreach ($faqs as $faq)
                    <div class="faq">
                        <h5 class="faq-title dark-text" style="font-size:19px;font-weight:700;">
                            {{$faq->question}} ?
                        </h5>
                        <p class="faq-text"  style="font-size:17px;">{{$faq->answer}}</p>
                        <button class="faq-toggle">
                            <i class="fas fa-angle-down"></i>
                        </button>
                    </div>
                @endforeach
            </div>
        </div>
    </div>
@endsection

@section('script')
    <script>
        $("#plan_type").change(function(){
            var checked = $(this).is(':checked');
            if(checked == true)
            {
                var type = "yearly";
            }
            else
            {
                var type = "monthly";
            }
        
            $('#type').val(type);
            $("#plan_form").submit();
        });
        var TxtType = function(el, toRotate, period) {
            this.toRotate = toRotate;
            this.el = el;
            this.loopNum = 0;
            this.period = parseInt(period, 10) || 2000;
            this.txt = '';
            this.tick();
            this.isDeleting = false;
        };

        TxtType.prototype.tick = function() {
            var i = this.loopNum % this.toRotate.length;
            var fullTxt = this.toRotate[i];

            if (this.isDeleting) {
                this.txt = fullTxt.substring(0, this.txt.length - 1);
            } else {
                this.txt = fullTxt.substring(0, this.txt.length + 1);
            }

            this.el.innerHTML = '<span class="wrap">' + this.txt + '</span>';

            var that = this;
            var delta = 200 - Math.random() * 100;

            if (this.isDeleting) {
                delta /= 2;
            }

            if (!this.isDeleting && this.txt === fullTxt) {
                delta = this.period;
                this.isDeleting = true;
            } else if (this.isDeleting && this.txt === '') {
                this.isDeleting = false;
                this.loopNum++;
                delta = 500;
            }

            setTimeout(function() {
                that.tick();
            }, delta);
        };

        window.onload = function() {
            var elements = document.getElementsByClassName('typewrite');
            for (var i = 0; i < elements.length; i++) {
                var toRotate = elements[i].getAttribute('data-type');
                var period = elements[i].getAttribute('data-period');
                if (toRotate) {
                    new TxtType(elements[i], JSON.parse(toRotate), period);
                }
            }
            // INJECT CSS
            var css = document.createElement("style");
            css.type = "text/css";
            css.innerHTML = ".typewrite > .wrap";
            document.body.appendChild(css);
        };

        const buttons = document.querySelectorAll(".faq-toggle");

        buttons.forEach((button) => {
        button.addEventListener("click", () =>
            button.parentElement.classList.toggle("active")
        );
        });


    </script>
@endsection
