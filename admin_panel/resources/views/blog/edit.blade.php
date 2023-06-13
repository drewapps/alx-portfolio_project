@extends('layouts.master')

@section('extra_css')
<link href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.11/summernote-lite.css" rel="stylesheet">
<style type="text/css">

</style>
@endsection

@section('content')
<div class="container">
    <div class="card card-success">
        <div class="card-header">
            <h3 class="card-title">Update Blog</h3>
        </div>

        <div class="card-body">
            {!! Form::open(['route' =>['blog.update',$blog->id],'method'=>'PATCH','files'=>true]) !!}
            {!! Form::hidden('id',$blog->id)!!}
            <div class="row">
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
                                        {!! Form::label('title','Title', ['class' => 'col-sm-3 col-form-label']) !!}
                                        <div class="col-sm-9">
                                            {!! Form::text('title',$blog->title,['class' => 'form-control','required','placeholder'=>'Enter Title']) !!}
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-12">
                                    <div class="form-group row">
                                        {!! Form::label('slug','Slug', ['class' => 'col-sm-3 col-form-label']) !!}
                                        <div class="col-sm-9">
                                            {!! Form::text('slug',$blog->slug,['class' => 'form-control','placeholder'=>'Enter Slug']) !!}
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-12">
                                    <div class="form-group row">
                                        {!! Form::label('description','Description', ['class' => 'col-sm-3 col-form-label']) !!}
                                        <div class="col-sm-9">
                                            <textarea name="description" id="desc_text" class="form-control" required>{{$blog->description}}</textarea>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-12">
                                    <div class="form-group row">
                                        {!! Form::label('meta_keyword','Meta Keywords', ['class' => 'col-sm-3 col-form-label']) !!}
                                        <div class="col-sm-9">
                                            {!! Form::text('meta_keyword',$blog->meta_keyword,['class' => 'form-control','required','placeholder'=>'Enter Meta Keywords']) !!}
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-12">
                                    <div class="form-group row">
                                        {!! Form::label('meta_description','Meta Description', ['class' => 'col-sm-3 col-form-label']) !!}
                                        <div class="col-sm-9">
                                            {!! Form::text('meta_description',$blog->meta_description,['class' => 'form-control','required','placeholder'=>'Enter Meta Description']) !!}
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="card">
                        <div class="card-body">
                            <div class="row">
                                <div class="col-12">
                                    <div class="form-group row">
                                        {!! Form::label('category','Category', ['class' => 'col-sm-3 col-form-label']) !!}
                                        <div class="col-sm-9">
                                            <select id="category_id" name="category_id" class="form-control" required>
                                                <option value="">Select Category</option>
                                                @foreach($category as $c)
                                                    <option value="{{$c->id}}" @if($blog->category_id == $c->id) selected @endif>{{$c->name}}</option>
                                                @endforeach
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-12">
                                    <div class="form-group row">
                                        {!! Form::label('image',' Select Image', ['class' => 'col-sm-3 col-form-label']) !!}
                                        <div class="col-sm-9"><input class="form-control" type="file" id="image" name="image"></div>
                                    </div>
                                    <div class="row"><div class="col-sm-3"></div><div class="col-sm-9" id="preview"><img type="image" class="shadow bg-white rounded" src="{{asset('uploads/'.$blog->image)}}" alt="Image" style="width: auto;height: 120px"/></div></div>
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
                <button type="submit" class="btn btn-success">Update</button>
                @endif
                </div>
            </div>
            {!! Form::close() !!}
        </div>
    </div>
</div>
@endsection

@section("script")
<script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.11/summernote-lite.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        $('#category_id').select2();
    });

    $('#desc_text').summernote({
        placeholder: '',
        tabsize: 2,
        height: 150
    });

    function imagePreview(fileInput) 
    { 
        if (fileInput.files && fileInput.files[0]) 
        {
            var fileReader = new FileReader();
            fileReader.onload = function (event) 
            {
                $('#preview').html('<img src="'+event.target.result+'" class="shadow bg-white rounded" alt="Select Image" style="width: auto;height: 120px"/>');
            };
            fileReader.readAsDataURL(fileInput.files[0]);
        }
    }

    $("#image").change(function () {
        imagePreview(this);
    });
</script>
@endsection