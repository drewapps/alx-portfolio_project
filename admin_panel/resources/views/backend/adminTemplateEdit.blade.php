@extends('layouts.master')

@section('content')
<div class="container">
    <div class="card card-success">
        <div class="card-header">
            <h3 class="card-title">Update Template</h3>
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

        {!! Form::open(['url' =>['admin/template-update'],'method'=>'POST','files'=>true]) !!}
        {!! Form::hidden('id',$data->id)!!}
        <div class="row">
            <div class="col-12">
                <div class="form-group row">
                    {!! Form::label('title','Title', ['class' => 'col-sm-2 col-form-label']) !!}
                    <div class="col-sm-4">
                        {!! Form::text('title',$data->title,['class' => 'form-control','required','readonly']) !!}
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-12">
                <div class="form-group row">
                    {!! Form::label('description','Description', ['class' => 'col-sm-2 col-form-label']) !!}
                    <div class="col-sm-4">
                        {!! Form::textarea('description',$data->description,['class' => 'form-control','rows'=> 5,'required']) !!}
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
</script>
@endsection