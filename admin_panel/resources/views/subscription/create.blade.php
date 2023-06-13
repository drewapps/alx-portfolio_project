@extends('layouts.master')

@section('heading')
<div class="mt-5">Add Subscription Plan</div>
@endsection

@section('extra_css')
<link href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.11/summernote-lite.css" rel="stylesheet">
<style type="text/css">

</style>
@endsection

@section('content')
    {!! Form::open(['route' => 'subscription-plan.store','method'=>'post','files'=>true]) !!}
    {!! Form::hidden('user_id',Auth::user()->id)!!}
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
        <div class="col-md-6">
            <div class="card">
                <div class="card-body">
                    <div class="row">
                        <div class="col-12">
                            <div class="form-group row">
                                {!! Form::label('plan_name','Plan Name', ['class' => 'col-sm-3 col-form-label']) !!}
                                <div class="col-sm-9">
                                    {!! Form::text('plan_name', null,['class' => 'form-control','required','placeholder'=>'Enter Plan Name']) !!}
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-12">
                            <div class="form-group row">
                                {!! Form::label('plan_price','Plan Price', ['class' => 'col-sm-3 col-form-label']) !!}
                                <div class="col-sm-9">
                                    {!! Form::number('plan_price', null,['class' => 'form-control','required','placeholder'=>'Enter Plan Price']) !!}
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-12">
                            <div class="form-group row">
                                {!! Form::label('discount_price','Discount Price', ['class' => 'col-sm-3 col-form-label']) !!}
                                <div class="col-sm-9">
                                    {!! Form::number('discount_price', null,['class' => 'form-control','required','placeholder'=>'Enter Discount Price']) !!}
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-12">
                            <div class="form-group row">
                                {!! Form::label('duration','Duration', ['class' => 'col-sm-3 col-form-label']) !!}
                                <div class="col-sm-9">
                                    <div class="row">
                                        <div class="col-6">
                                            {!! Form::number('duration', null,['class' => 'form-control','required']) !!}
                                        </div>
                                        <div class="col-6">
                                            <select id="duration_type" name="duration_type" class="form-control" required>
                                                <option value="">Select Plan</option>
                                                <option value="Day">Day</option>
                                                <option value="Week">Week</option>
                                                <option value="Month">Month</option>
                                                <option value="Year">Year</option>
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
                                {!! Form::label('total_words','Total Words included in Plan', ['class' => 'col-sm-3 col-form-label']) !!}
                                <div class="col-sm-9">
                                    {!! Form::number('total_words', null,['class' => 'form-control','required','placeholder'=>'Enter Total Words']) !!}
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-12">
                            <div class="form-group row">
                                {!! Form::label('number_of_images','Number of Images included in Plan', ['class' => 'col-sm-3 col-form-label']) !!}
                                <div class="col-sm-9">
                                    {!! Form::number('number_of_images', null,['class' => 'form-control','required','placeholder'=>'Enter Number of Images']) !!}
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-12">
                            <div class="form-group row">
                                {!! Form::label('plan_type','Plan Type', ['class' => 'col-sm-3 col-form-label']) !!}
                                <div class="col-sm-9">
                                    <select id="plan_type" name="plan_type" class="form-control" required>
                                        <option value="">Select Type</option>
                                        <option value="monthly">Monthly</option>
                                        <option value="yearly">Yearly</option>
                                        <option value="trial">Trial</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-12">
                            <div class="form-group row">
                                {!! Form::label('description','Description', ['class' => 'col-sm-3 col-form-label']) !!}
                                <div class="col-sm-9">
                                    <textarea name="description" id="desc_text" class="form-control" required></textarea>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-12">
                            <div class="form-group row">
                                {!! Form::label('image',' Select Image', ['class' => 'col-sm-3 col-form-label']) !!}
                                <div class="col-sm-9"><input class="form-control" type="file" id="image" name="image" onchange="imagePreview()" accept=".jpg, .png, jpeg, .PNG, .JPG, .JPEG" required></div>
                            </div>
                            <div class="row"><div class="col-sm-3"></div><div class="col-sm-9" id="preview"><img type="image" class="shadow bg-white rounded" src="{{asset('assets/images/no-image.png')}}" alt="Image" style="width: auto;height: 150px"/></div></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="card">
                <div class="card-body">
                    <b>Include Plan Details:</b>
                    <div class="card mt-3">
                        <div class="card-body text-center">
                            <span id="add_plan"><i class="fa-solid fa-plus fa-xl"></i></span>
                        </div>
                    </div>
                    <div id="add_text"></div>
                </div>
            </div> 

            <div class="card">
                <div class="card-body">
                    <b>Not Include Plan Details:</b>
                    <div class="card mt-3">
                        <div class="card-body text-center">
                            <span id="add_plan1"><i class="fa-solid fa-plus fa-xl"></i></span>
                        </div>
                    </div>
                    <div id="add_text1"></div>
                </div>
            </div> 
        </div>
    </div>

    <div class="row">
        <div class="col-md-12 m-3 text-center">
        @if(Auth::user()->user_type == "Demo")
        <button type="button" class="btn btn-success ToastrButton">Save</button>
        @else
        {!! Form::submit('Save', ['class' => 'btn btn-success']) !!}
        @endif
        </div>
    </div>
    {!! Form::close() !!}
@endsection

@section("script")
<script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.11/summernote-lite.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        $('#duration_type').select2();
        $('#package_access').select2();
        $('#plan_type').select2();
    });

    $('#desc_text').summernote({
        placeholder: '',
        tabsize: 2,
        height: 150
    });

    $(function() {
        var count = 1;
        $('#add_plan').on('click', function(e){
            e.preventDefault();
            $('#add_text').append('<div class="row mb-2"><div class="col-11"><input type="text" class="form-control" name="detail[data' + count +']" placeholder="Enter Detail"></div><div class="col-1"><button type="button" class="btn btn-danger remove"><i class="fa fa-xmark"></i></button></div></div>');
            count++;
        });
        $(document).on('click', 'button.remove', function( e ) {
            e.preventDefault();
            $(this).closest( 'div.row' ).remove();
        });
    });

    function imagePreview() 
    { 
        var total_file=document.getElementById("image").files.length;
        for(var i=0;i<total_file;i++)
        {
            $('#preview').html("<img class='img-responsive mr-3 shadow bg-white rounded' src='"+URL.createObjectURL(event.target.files[i])+"' style='width:150px;height:auto;'>");
        }
    }

    $(function() {
        var count = 1;
        $('#add_plan1').on('click', function(e){
            e.preventDefault();
            $('#add_text1').append('<div class="row mb-2"><div class="col-11"><input type="text" class="form-control" name="detail1[data' + count +']" placeholder="Enter Detail"></div><div class="col-1"><button type="button" class="btn btn-danger remove1"><i class="fa fa-xmark"></i></button></div></div>');
            count++;
        });
        $(document).on('click', 'button.remove1', function( e ) {
            e.preventDefault();
            $(this).closest( 'div.row' ).remove();
        });
    });
</script>
@endsection