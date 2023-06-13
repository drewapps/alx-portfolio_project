@extends("layouts.master")

@section('extra_css')
<style type="text/css">
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
                Today Generated Image
            </h3>
        </div> 
      
      <div class="card-body table-responsive">
        <table class="table table-bordered table-striped" id="data_table">
          <thead class="thead-inverse">
            <tr>
              <th>#</th>
              <th>Image</th>
              <th>Name</th>
              <th>Resolution</th>
              <th>User</th>
            </tr>
          </thead>
          <tbody>
          @foreach($today_image as $row)
            <tr>
                <td>{{$row->id}}</td>
                <td><img src="{{asset('uploads/aiImage/'.$row->image)}}" class="mr-2" width="50px" height="50px"></td>
                <td>{{$row->description}}</td>
                <td>{{$row->resolution}}</td>
                <td><a href="{{url('admin/user/'.$row->user->id) }}" class="text-primary">{{$row->user->name}}</a></td>
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
</script>
@endsection
