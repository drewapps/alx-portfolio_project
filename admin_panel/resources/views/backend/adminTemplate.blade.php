@extends("layouts.master")

@section('extra_css')
<style type="text/css">
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

.notification {
  position: relative;
  display: inline-block;
}

.notification .badge {
  position: absolute;
  top: -7px;
  right: -7px;
}

.fa-google-plus {
  color: blue;
}

img {
  display: inline-block;
}
</style>
@endsection
@section('content')
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
    <div class="card card-primary">
      <div class="card-header">
        <h3 class="card-title float-left">
            Templates
        </h3>
      </div> 

      <div class="card-body table-responsive table-bordered table-striped">
        <table class="table" id="data_table">
          <thead class="thead-inverse">
            <tr>
                <th>#</th>
                <th>Title</th>
                <th>Type</th>
                <th>Status</th>
                <th>Action</th>
            </tr>
          </thead>
          <tbody>
          @foreach($data as $row)
            <tr>
                <td class="align-middle">{{$row->id}}</td>
                <td><img src="@if($row->image) {{asset('assets/images/social/'.$row->image)}} @else {{asset('assets/images/no-image.png')}} @endif" class="mr-2" alt="Image" width="40" height="40"></div> <b>{{$row->title}}</b></td>
                <td class="align-middle">{{$row->type}}</td>
                <td style="align-middle">
                    <label class="switch mt-2">
                        <input type="checkbox" name="status" data-id="{{$row->id}}" value="1" class="status" @if($row->status==1) checked @endif>
                        <span class="slider round"></span>
                    </label>
                </td>
                <td class="align-middle">
                    <div class="btn-group">
                        <a href="{{url('admin/template/'.$row->id.'/edit') }}"><button type="button" class="btn btn-success"><span aria-hidden="true" class="fa fa-edit"></span></button></a>
                    </div>
                </td>
            </tr>
          @endforeach
          </tbody>
        </table>
      </div>
    </div>
  </div>
</div>
@endsection

@section('script')
<script type="text/javascript">
  $('.ToastrButton').click(function() {
    toastr.error('This Action Not Available Demo User');
  });

  $(".status").change(function(){
    var checked = $(this).is(':checked');
    var id = $(this).data("id");
    
    $.ajax({
      type: "POST",
      url: "{{url('admin/template-status')}}",
      data: { checked : checked , id : id},
      headers: { 'X-CSRF-TOKEN': $('meta[name="csrf-token"]').attr('content') },
      success: function(data) {
        new PNotify({
          title: 'Success!',
          text: "Template Status Has Been Changed.",
          type: 'success'
        });
      },
    });
  });
</script>
@endsection
