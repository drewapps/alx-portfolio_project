@extends("layouts.master")

@section('extra_css')
<style type="text/css">

</style>
@endsection

@section('content')
<div class="row">
  <div class="col-md-12">
    <div class="row">
      <div class="col-md col-sm-12">
        <div class="card overflow-hidden border">
          <div class="card-body">
            <i class="fa-solid fa-comment text-primary fa-2xl mt-3 float-right"></i>	
            <p class="mb-3 fs-12 font-weight-bold mt-1">Open Tickets</p>
            <h2 class="mb-0"><span class="number-font-chars">{{$open_ticket}}</span></h2>									
          </div>
        </div>
      </div>
      <div class="col-md col-sm-12">
        <div class="card border">
          <div class="card-body">
            <i class="fa-solid fa-comment-dots text-warning fa-2xl mt-3 float-right yellow"></i>	
            <p class=" mb-3 fs-12 font-weight-bold mt-1">Replied Tickets</p>
            <h2 class="mb-0"><span class="number-font-chars">{{$replied_ticket}}</span></h2>
          </div>
        </div>
      </div>
      <div class="col-md col-sm-12">
        <div class="card border">
          <div class="card-body">
            <i class="fa-solid fa-comments fa-2xl text-info mt-3 float-right"></i>	
            <p class=" mb-3 fs-12 font-weight-bold mt-1">Pending Tickets</p>
            <h2 class="mb-0"><span class="number-font-chars">{{$pending_ticket}}</span></h2>
          </div>
        </div>
      </div>
      <div class="col-md col-sm-12">
        <div class="card border">
          <div class="card-body">
            <i class="fa-solid fa-clipboard-check text-success fa-2xl mt-3 float-right"></i>	
            <p class=" mb-3 fs-12 font-weight-bold mt-1">Resolved Tickets</p>
            <h2 class="mb-0"><span class="number-font-chars">{{$resolved_ticket}}</span></h2>
          </div>
        </div>
      </div>
      <div class="col-md col-sm-12">
        <div class="card border">
          <div class="card-body">
            <i class="fa-solid fa-clipboard-list fa-2xl text-danger mt-3 float-right"></i>	
            <p class=" mb-3 fs-12 font-weight-bold mt-1">Closed Tickets</p>
            <h2 class="mb-0"><span class="number-font-chars">{{$closed_ticket}}</span></h2>
          </div>
        </div>
      </div>
    </div>

    <div class="card card-primary">
        <div class="card-header">
            <h3 class="card-title float-left">
                Support Request
            </h3>
            <a data-toggle="modal" data-target="#deleteModal" class="btn btn-danger float-right">Delete</a>
        </div> 
      
      <div class="card-body table-responsive table-bordered table-striped">
        <table class="table" id="data_table_ticket">
          <thead class="thead-inverse">
            <tr>
                <th>#</th>
                <th>Ticket ID</th>
                <th>Created By</th>
                <th>Status</th>
                <th>Category</th>
                <th>Subject</th>
                <th>Priority</th>
                <th>Last Updated</th>
                <th>Action</th>
            </tr>
          </thead>
          <tbody>
          @foreach($data as $row)
            <tr>
                <td class="align-middle"><input type="checkbox" class="delete" name="delete" value="{{$row->id}}"></td>
                <td class="align-middle"><a href="{{url('admin/support-requests/'.$row->ticket_id) }}" class="text-primary">{{$row->ticket_id}}</a></td>
                <td class="align-middle">{{$row->user->name}}</td>
                <td class="align-middle">{{$row->status}}</td>
                <td class="align-middle">{{$row->category}}</td>
                <td class="align-middle">{{$row->subject}}</td>
                <td class="align-middle">{{$row->priority}}</td>
                <td class="align-middle">{{date('d M Y H:i A', strtotime($row->updated_at))}}</td>
                <td class="align-middle">
                    <div class="btn-group">
                        <a href="{{url('admin/support-requests/'.$row->ticket_id) }}"><button type="button" class="btn btn-success"><span aria-hidden="true" class="fa fa-eye"></span></button></a>
                        <a data-id="{{$row->id}}" data-toggle="modal" data-target="#myModal"><button type="button" class="btn btn-danger ml-2"><span aria-hidden="true" class="fa fa-trash"></span></button></a>
                    </div>
                    {!! Form::open(['url' => 'admin/support-requests/'.$row->ticket_id,'method'=>'DELETE','class'=>'form-horizontal','id'=>'form_'.$row->id]) !!}
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
                <form action="{{url('admin/multi-delete-support-request')}}" method="post">
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

    var table = $('#data_table_ticket').DataTable({
      "order": [[ 7, 'desc' ]],
     columnDefs: [ { orderable: false, targets: [0] } ],
     // individual column search
     "initComplete": function() {
      table.columns().every( function () {
          var that = this;
          $('input', this.footer()).on('keyup change', function () {
            // console.log($(this).parent().index());
              that.search(this.value).draw();
          });
        });
      }
    });
</script>
@endsection
