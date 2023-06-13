@extends("layouts.master")

@section('extra_css')
<style type="text/css">
.notification {
  position: relative;
  display: inline-flex;
}

@media (max-width: 770px){
  .notification {
    display: block;
  }
}

.notification .badge {
  position: absolute;
  top: -7px;
  right: -7px;
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
                User Delete Request
            </h3>
            <a data-toggle="modal" data-target="#deleteModal" class="btn btn-danger float-right">Delete</a>
        </div> 
      
      <div class="card-body table-responsive">
        <table class="table table-bordered table-striped" id="data_table">
          <thead class="thead-inverse">
            <tr>
              <th>#</th>
              <th>Id</th>
              <th>Name</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
          @foreach($user as $row)
            <tr>
                <td class="align-middle"><input type="checkbox" class="delete" name="delete" value="{{$row->user->id}}"></td>
                <td class="align-middle">{{$row->id}}</td>
                <td><div class="notification"><img src="@if($row->user->image) @if(substr($row->user->image, 0, 4)=='http') {{$row->user->image}} @else {{asset('uploads/'.$row->user->image)}} @endif @else {{asset('assets/images/no-user.jpg')}} @endif" class="rounded-circle" alt="Image" style="height:40px;width:40px;"/><a href="{{url('admin/user/'.$row->user->id) }}" class="ml-md-3 mt-2" style="font-size:15px;"><b>{{$row->user->name}}</b></a></div></td>
                <td class="align-middle">
                    <div class="btn-group">
                        <a data-id="{{$row->id}}" data-toggle="modal" data-target="#myModal" style="cursor:pointer;"><button type="button" class="btn btn-danger ml-2"><span aria-hidden="true" class="fa fa-trash"></span></button></a>
                    </div>
                </td>
                {!! Form::open(['url' => 'admin/delete-user','method'=>'POST','class'=>'form-horizontal','id'=>'form_'.$row->id]) !!}
                {!! Form::hidden("id",$row->user->id) !!}
                {!! Form::close() !!}
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
        <button id="del_btn" class="btn btn-danger" type="button" data-submit="" style="background-color:#dc3545;">Delete</button>
        @endif
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>
<!-- Modal -->

<!-- delete Modal -->
<div id="deleteModal" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Delete</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <p>Are you sure you want to Delete all Check Record ?</p>
            </div>
            <div class="modal-footer">
                @if(Auth::user()->user_type == "Demo")
                <button type="button" class="btn btn-danger ToastrButton">Delete</button>
                @else
                <form action="{{url('admin/multi-delete-user')}}" method="post">
                  @csrf
                  <input type="hidden" name="deleted_ids" id="deleted_ids">
                  <button class="btn btn-danger" type="submit">Delete</button>
                </form>
                @endif
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<!-- delete Modal -->
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

    $('#deleteModal').on('show.bs.modal', function(e) {
      var searchIDs = [];

      $("input:checkbox:checked.delete").map(function(){
        searchIDs.push($(this).val());
      });

      document.getElementById('deleted_ids').value = JSON.stringify(searchIDs);
    });
</script>
@endsection
