@extends('layouts.master')

@section('extra_css')
<link href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.11/summernote-lite.css" rel="stylesheet">
<style type="text/css">
.select2-container {
    display: block;
}
</style>
@endsection

@section('content')
    {!! Form::open(['route' =>['plan.update',$plan->id],'method'=>'PATCH','files'=>true]) !!}
    {!! Form::hidden('user_id',Auth::user()->id)!!}
    {!! Form::hidden('id',$plan->id)!!}
    <div class="row mt-5">
        <div class="col-12">
            @if (count($errors) > 0)
                <div class="alert alert-danger">
                    <ul>
                        @foreach ($errors->all() as $error)
                        <li>{{ $error }}</li>
                        @endforeach
                    </ul>
                </div>
            @endif
        </div>
        <div class="col-md-12">
            <div class="card card-primary">
                <div class="card-header">
                    <h3 class="card-title">Update Plan</h3>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-12">
                            <div class="form-group row">
                                {!! Form::label('plan_name','Plan Name', ['class' => 'col-sm-3 col-form-label']) !!}
                                <div class="col-sm-4">
                                    {!! Form::text('plan_name',$plan->plan_name,['class' => 'form-control','required','placeholder'=>'Enter Plan Name']) !!}
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-12">
                            <div class="form-group row">
                                {!! Form::label('plan_price','Plan Price', ['class' => 'col-sm-3 col-form-label']) !!}
                                <div class="col-sm-4">
                                    {!! Form::number('plan_price',$plan->plan_price,['class' => 'form-control','required','placeholder'=>'Enter Plan Price']) !!}
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-12">
                            <div class="form-group row">
                                {!! Form::label('total_words','Total Words included in Plan', ['class' => 'col-sm-3 col-form-label']) !!}
                                <div class="col-sm-4">
                                    {!! Form::number('total_words', $plan->total_words,['class' => 'form-control','required','placeholder'=>'Enter Total Words']) !!}
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-12">
                            <div class="form-group row">
                                {!! Form::label('number_of_images','Number of Images included in Plan', ['class' => 'col-sm-3 col-form-label']) !!}
                                <div class="col-sm-4">
                                    {!! Form::number('number_of_images',$plan->number_of_images,['class' => 'form-control','required','placeholder'=>'Enter Number of Images']) !!}
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-12">
                            <div class="form-group row">
                                <div class="col-sm-3">
                                    <label for="rewarded_enable" class="col-form-label">Remove Ads</label><br>
                                    <small class="text-primary">* If you Enable Remove Ads in This Plan, the User can not Show Ads in the App from the Selected Period.</small>
                                </div>
                                <div class="col-sm-4">
                                    <label class="cl-switch cl-switch-blue mt-2">
                                        <input type="checkbox" id="rewarded_enable" value="1" name="rewarded_enable" @if($plan->rewarded_enable == 1) checked @endif>
                                        <span class="switcher"></span>
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row reward-duration" style="@if($plan->rewarded_enable == 0)display:none;@endif">
                        <div class="col-12">
                            <div class="form-group row">
                                {!! Form::label('duration','Duration', ['class' => 'col-sm-3 col-form-label']) !!}
                                <div class="col-sm-4">
                                    <div class="row">
                                        <div class="col-6">
                                            <input class="form-control" value="{{$plan->duration}}" id="duration" placeholder="Enter Duration" name="duration" type="number" @if($plan->rewarded_enable == 1) required @endif>
                                        </div>
                                        <div class="col-6">
                                            <select id="duration_type" name="duration_type" class="form-control" @if($plan->rewarded_enable == 1) required @endif>
                                                <option value="">Select Type</option>
                                                <option value="Day" @if($plan->duration_type == "Day") selected @endif>Day</option>
                                                <option value="Week" @if($plan->duration_type == "Week") selected @endif>Week</option>
                                                <option value="Month" @if($plan->duration_type == "Month") selected @endif>Month</option>
                                                <option value="Year" @if($plan->duration_type == "Year") selected @endif>Year</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-12">
                            <div class="form-group row">
                                {!! Form::label('google_product_enable','Google In Purchases', ['class' => 'col-sm-3 col-form-label']) !!}
                                <div class="col-sm-4">
                                    <label class="cl-switch cl-switch-blue mt-2">
                                        <input type="checkbox" id="google_product_enable" value="1" name="google_product_enable" @if($plan->google_product_enable == 1) checked @endif>
                                        <span class="switcher"></span>
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row google-product" style="@if($plan->google_product_enable == 0)display:none;@endif">
                        <div class="col-12">
                            <div class="form-group row">
                                {!! Form::label('google_product_id','Google In-app Product Id', ['class' => 'col-sm-3 col-form-label']) !!}
                                <div class="col-sm-4">
                                    <input class="form-control" value="{{$plan->google_product_id}}" id="google_product_id" placeholder="Enter Google In-app Product Id" name="google_product_id" type="text" @if($plan->google_product_enable == 1) required @endif>
                                </div>
                            </div>
                            <div class="form-group">
                                <small class="text-primary">* Must be the plan price the same as in the Play console in-app purchase pricing template creation.</small>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12 m-3 text-center">
            @if(Auth::user()->user_type == "Demo")
            <button type="button" class="btn btn-success ToastrButton">Update</button>
            @else
            {!! Form::submit('Update', ['class' => 'btn btn-success']) !!}
            @endif
        </div>
    </div>
    {!! Form::close() !!}
@endsection

@section("script")
<script type="text/javascript">
    $(document).ready(function() {
        $('#duration_type').select2();

        $('#google_product_enable').change(function(){
            if($(this).is(':checked'))
            {
                $(".google-product").css("display","block");
                $("#google_product_id").prop('required',true);
            }
            else
            {
                $(".google-product").css("display","none");
                $("#google_product_id").prop('required',false);
            }
        });

        $('#rewarded_enable').change(function(){
            if($(this).is(':checked'))
            {
                $(".reward-duration").css("display","block");
                $("#duration").prop('required',true);
                $("#duration_type").prop('required',true);
            }
            else
            {
                $(".reward-duration").css("display","none");
                $("#duration").prop('required',false);
                $("#duration_type").prop('required',false);
            }
        });
    });
</script>
@endsection