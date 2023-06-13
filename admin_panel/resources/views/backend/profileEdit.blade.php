@extends('layouts.master')

@section('extra_css')
<style type="text/css">

</style>
@endsection

@section('content')
<div class="container">
    <div class="card card-success">
        <div class="card-header">
            <h3 class="card-title">Update Profile Detail</h3>
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

        {!! Form::open(['url' => 'admin/update-profile','method'=>'post','files'=>true]) !!}
        <div class="row mt-3">
            <div class="col-12">
                <div class="form-group row">
                    {!! Form::label('name','Name', ['class' => 'col-xl-3 col-md-3 col-3 col-form-label']) !!}
                    <div class="col-xl-9 col-md-9 col-9">
                        {!! Form::text('name',$user->name,['class' => 'form-control','required']) !!}
                    </div>
                </div>
            </div>
        </div>
        
        <div class="row">
            <div class="col-12">
                <div class="form-group row">
                    {!! Form::label('email','Email', ['class' => 'col-xl-3 col-md-3 col-3 col-form-label']) !!}
                    <div class="col-xl-9 col-md-9 col-9">
                        {!! Form::email('email',$user->email,['class'=>'form-control','required']) !!}
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-12">
                <div class="form-group row">
                    {!! Form::label('password', 'Password', ['class' => 'col-xl-3 col-md-3 col-3 col-form-label']) !!}
                    <div class="col-xl-9 col-md-9 col-9">
                        {!! Form::password('password', ['class' => 'form-control']) !!}
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-12">
                <div class="form-group row">
                    {!! Form::label('image','Select Image', ['class' => 'col-xl-3 col-md-3 col-3 col-form-label']) !!}
                    <div class="col-xl-9 col-md-9 col-9">
                        <input class="form-control" type="file" id="image" name="image">
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-3"></div>
                    <div class="col-sm-9 mb-3" id="preview">
                        <img src="@if($user->image)@if(substr($user->image, 0, 4)=="http") {{$user->image}} @else {{asset('uploads/'.$user->image)}} @endif @else {{asset('assets/images/no-user.jpg')}} @endif" class="mt-2" width="100px" height="100px">
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
        </div>
    </div>
</div>
@endsection

@section("script")
<script type="text/javascript">
    $(document).ready(function() {
        $('#status_id').select2();
    });

    function imagePreview(fileInput) 
    { 
        if (fileInput.files && fileInput.files[0]) 
        {
            var fileReader = new FileReader();
            fileReader.onload = function (event) 
            {
                $('#preview').html('<img src="'+event.target.result+'" class="shadow bg-white rounded" width="auto" alt="Select Image" height="120px"/>');
            };
            fileReader.readAsDataURL(fileInput.files[0]);
        }
    }

    $("#image").change(function () {
        imagePreview(this);
    });

</script>
@endsection