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
                Subscriptions Plan
            </h3>
            <a href="{{ route('subscription-plan.create')}}" class="btn btn-success float-right">Add New</a>
        </div> 
      
      <div class="card-body table-responsive table-bordered table-striped">
        <table class="table" id="data_table">
          <thead class="thead-inverse">
            <tr>
              <th>#</th>
              <th>Plan Name</th>
              <th>Duration</th>
              <th>Plan Price</th>
              <th>Dis. Price</th>
              <th>Plan Type</th>
              <th>Created</th>
              <th>Status</th>
              <th>Most Popular</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
          @foreach($data as $row)
            <tr>
              <td class="align-middle">{{$row->id}}</td>
              <td><img class="rounded-circle" src="{{asset('uploads/'.$row->image)}}" width="50px" height="50px"><b class="ml-3" style="font-size:16px;">{{$row->plan_name}}</b></td>
              <td class="align-middle">{{$row->duration}} {{$row->duration_type}}</td>
              <td class="align-middle">{{number_format($row->plan_price,2)}}</td>
              <td class="align-middle">{{number_format($row->discount_price,2)}}</td>
              <td class="align-middle"><span class="btn btn-primary rounded-pill p-1" style="font-size:18px;">{{ucfirst($row->plan_type)}}</span></td>
              <td class="align-middle">{{date('d/m/Y', strtotime($row->created_at))}}</td>
              <td class="align-middle">
                <label class="switch">
                    <input type="checkbox" name="status" data-id="{{$row->id}}" value="1" class="status" @if($row->status==1) checked @endif>
                    <span class="slider round"></span>
                </label>
              </td>
              <td class="align-middle">
                <label class="switch">
                    <input type="checkbox" name="most_popular" data-id="{{$row->id}}" value="1" class="most_popular" @if($row->most_popular==1) checked @endif>
                    <span class="slider round"></span>
                </label>
              </td>
              <td class="align-middle">
                <div class="btn-group">
                  <a href="{{url('admin/subscription-plan/'.$row->id.'/edit') }}"><button type="button" class="btn btn-success"><span aria-hidden="true" class="fa fa-edit"></span></button></a>
                  <a data-id="{{$row->id}}" data-toggle="modal" data-target="#myModal"><button type="button" class="btn btn-danger ml-2"><span aria-hidden="true" class="fa fa-trash"></span></button></a>
                </div>
                {!! Form::open(['url' => 'admin/subscription-plan/'.$row->id,'method'=>'DELETE','class'=>'form-horizontal','id'=>'form_'.$row->id]) !!}
                {!! Form::hidden("id",$row->id) !!}
                {!! Form::close() !!}
              </td>
            </tr>
          @endforeach
          </tbody>
        </table>
      </div>
    </div>
  </div>
</div>

  <!-- Modal -->
  <div id="myModal" class="modal fade" role="dialog">
    <div class="modal-dialog">
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <h4 class="modal-title">Delete</h4>
          <button type="button" class="close" data-dismiss="modal">&times;</button>
        </div>
        <div class="modal-body">
          <p>Are you sure you want to Delete ?</p>
        </div>
        <div class="modal-footer">
          @if(Auth::user()->user_type == "Demo")
          <button type="button" class="btn btn-danger ToastrButton">Delete</button>
          @else
          <button id="del_btn" class="btn btn-danger" type="button" data-submit="">Delete</button>
          @endif
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>
  <!-- Modal -->
@endsection

@section('script')
<script type="text/javascript">
    $("#del_btn").on("click",function(){
        var id=$(this).data("submit");
        $("#form_"+id).submit();
    });

    $('#myModal').on('show.bs.modal', function(e) {
        var id = e.relatedTarget.dataset.id;
        $("#del_btn").attr("data-submit",id);
    });

    $('.ToastrButton').click(function() {
      toastr.error('This Action Not Available Demo User');
    });

    $(".status").change(function(){
      var checked = $(this).is(':checked');
      var id = $(this).data("id");
     
      $.ajax({
        type: "POST",
        url: "{{url('admin/subscription-plan-status')}}",
        data: { checked : checked , id : id},
        headers: { 'X-CSRF-TOKEN': $('meta[name="csrf-token"]').attr('content') },
        success: function(data) {
          new PNotify({
            title: 'Success!',
            text: "Subscription Plan Status Has Been Changed.",
            type: 'success'
          });
        },
      });
    });

    $(".most_popular").change(function(){
      var checked = $(this).is(':checked');
      var id = $(this).data("id");
     
      $.ajax({
        type: "POST",
        url: "{{url('admin/subscription-most-popular')}}",
        data: { checked : checked , id : id},
        headers: { 'X-CSRF-TOKEN': $('meta[name="csrf-token"]').attr('content') },
        success: function(data) {
          location.reload();
        },
      });
    });
</script>
@endsection
