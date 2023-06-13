@extends('layouts.master')

@section('content')
<div class="container">
    <div class="card card-success">
        <div class="card-header">
          <h3 class="card-title">Add User</h3>
        </div>

        <div class="card-body">
          {!! Form::open(['route' => 'user.store','method'=>'post','files'=>true]) !!}
          {!! Form::hidden('user_id',Auth::user()->id)!!}
          <div class="row">
            <div class="col-md-12">
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
                      {!! Form::label('name','Name', ['class' => 'col-sm-2 col-form-label']) !!}
                      <div class="col-sm-6">
                        {!! Form::text('name', null,['class' => 'form-control','required']) !!}
                      </div>
                    </div>
                  </div>
                </div>
                
                <div class="row">
                  <div class="col-12">
                    <div class="form-group row">
                      {!! Form::label('email','Email', ['class' => 'col-sm-2 col-form-label']) !!}
                      <div class="col-sm-6">
                        {!! Form::email('email',null,['class'=>'form-control','required']) !!}
                      </div>
                    </div>
                  </div>
                </div>

                <!-- <div class="row">
                  <div class="col-12">
                    <div class="form-group row">
                      {!! Form::label('mobile_no','Mobile No', ['class' => 'col-sm-2 col-form-label']) !!}
                      <div class="col-sm-6">
                        {!! Form::number('mobile_no', null,['class' => 'form-control','required']) !!}
                      </div>
                    </div>
                  </div>
                </div> -->

                <div class="row">
                  <div class="col-12">
                    <div class="form-group row">
                      {!! Form::label('password', 'Password', ['class' => 'col-sm-2 col-form-label']) !!}
                      <div class="col-sm-6">
                        {!! Form::password('password', ['class' => 'form-control','required']) !!}
                      </div>
                    </div>
                  </div>
                </div>

                <div class="row">
                  <div class="col-12">
                    <div class="form-group row">
                      {!! Form::label('image','Select Image', ['class' => 'col-sm-2 col-form-label']) !!}
                      <div class="col-sm-6">
                        <input class="form-control" type="file" id="image" name="image" required>
                      </div>
                    </div>
                    <div class="row">
                      <div class="col-2"></div>
                      <div class="col-6" id="preview"><img type="image" class="shadow bg-white rounded" src="{{asset('assets/images/no-user.jpg')}}" alt="Image" style="width: 120px;height: 120px" /></div>  
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
@endsection

@section("script")
<script type="text/javascript">
  function imagePreview(fileInput) 
  { 
      if (fileInput.files && fileInput.files[0]) 
      {
          var fileReader = new FileReader();
          fileReader.onload = function (event) 
          {
              $('#preview').html('<img src="'+event.target.result+'" class="shadow bg-white rounded" width="120px" alt="Select Image" height="120px"/>');
          };
          fileReader.readAsDataURL(fileInput.files[0]);
      }
  }

  $("#image").change(function () {
      imagePreview(this);
  });
</script>
@endsection