@extends('layouts.master')

@section('content')
<div class="container">
    <div class="card card-success">
        <div class="card-header">
            <h3 class="card-title">Update Coupon Code</h3>
        </div>

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

        {!! Form::open(['route' =>['coupon-code.update',$couponCode->id],'method'=>'PATCH','files'=>true]) !!}
        {!! Form::hidden('user_id',Auth::user()->id)!!}
        {!! Form::hidden('id',$couponCode->id)!!}
        <div class="row">
            <div class="col-12">
                <div class="form-group row">
                    {!! Form::label('code','Code', ['class' => 'col-sm-2 col-form-label']) !!}
                    <div class="col-sm-4">
                        {!! Form::text('code',$couponCode->code,['class' => 'form-control','placeholder'=>'Enter Coupon Code','required']) !!}
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-12">
                <div class="form-group row">
                    {!! Form::label('discount','Discount(%)', ['class' => 'col-sm-2 col-form-label']) !!}
                    <div class="col-sm-4">
                        {!! Form::number('discount',$couponCode->discount,['class' => 'form-control','placeholder'=>'Enter Discount','required']) !!}
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-12">
                <div class="form-group row">
                    {!! Form::label('limit','Limit', ['class' => 'col-sm-2 col-form-label']) !!}
                    <div class="col-sm-4">
                        {!! Form::number('limit',$couponCode->limit,['class' => 'form-control','placeholder'=>'Enter limit','required']) !!}
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-12">
                <div class="form-group row">
                    {!! Form::label('subscription_plan','Select Subscription Plan', ['class' => 'col-sm-2 col-form-label']) !!}
                    <div class="col-sm-4">
                        <select id="subscription_id" name="subscription_id" class="form-control" required>
                            <option value="">Select Plan</option>
                            @foreach($subscription as $plan)
                            <option value="{{$plan->id}}" @if($plan->id == $couponCode->subscription_id) selected @endif>{{$plan->plan_name}}</option>
                            @endforeach
                        </select>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
                <div class="col-12">
                    <div class="form-group row">
                        {!! Form::label('title','Title', ['class' => 'col-sm-2 col-form-label']) !!}
                        <div class="col-sm-4">
                            {!! Form::text('title', $couponCode->title,['class' => 'form-control','placeholder'=>'Enter Coupon Title','required']) !!}
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-12">
                    <div class="form-group row">
                        {!! Form::label('description','Description', ['class' => 'col-sm-2 col-form-label']) !!}
                        <div class="col-sm-4">
                            {!! Form::textarea('description', $couponCode->description,['class' => 'form-control','rows' => 4 ,'placeholder'=>'Enter Coupon Description','required']) !!}
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-12">
                    <div class="form-group row">
                        {!! Form::label('valid_until','Valid Until', ['class' => 'col-sm-2 col-form-label']) !!}
                        <div class="col-sm-4">
                            {!! Form::text('valid_until',$couponCode->valid_until,['class' => 'form-control datepicker','required',"autocomplete"=>"off"]) !!}
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
        </div>
    </div>
</div>
@endsection

@section("script")
<script type="text/javascript">
    $('#subscription_id').select2();
    $('.datepicker').datepicker({
        dateFormat: 'yy-mm-dd',
        minDate:'today',  
    });
</script>
@endsection