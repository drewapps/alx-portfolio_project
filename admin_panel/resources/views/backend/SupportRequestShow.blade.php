@extends("layouts.master")

@section('extra_css')
<link href="{{asset('assets/plugins/awselect/awselect.min.css')}}" rel="stylesheet" />
<style type="text/css">
.input-box .form-control {
    font-family: "Poppins", sans-serif;
    font-size: 12px;
    color: #1e1e2d;
    -webkit-appearance: none;
    -moz-appearance: none;
    outline: none;
    appearance: none;
    background-color: #f5f9fc;
    border-color: transparent;
    border-radius: 0.5rem;
    border-width: 1px;
    font-weight: 700;
    line-height: 1rem;
    padding: 0.75rem 1rem;
    width: 100%;
}
.awselect {
  text-align: left;
  margin-bottom: 1.5rem;
  font-size: 12px !important;
  font-weight: 600;
}
.awselect .bg {
  border-radius: 10px;
}

#tts-project .awselect {
  margin-bottom: 1rem;
}

.awselect .front_face {
  padding: 0.75rem 1rem !important;
}

.awselect .front_face > .bg {
  background: #f5f9fc !important;
  color: #1e1e2d;
  border-radius: 0.5rem;
  padding: 0.75rem 1rem;
}

.awselect .front_face .current_value {
  font-family: "Poppins", sans-serif !important;
  font-size: 12px;
  font-weight: 700 !important;
}

.awselect .back_face {
  padding: 0px !important;
  float: left;
}

.awselect.animate2 > .bg {
  box-shadow: 0px 4px 7px -2px rgba(0, 0, 0, 0.5);
}

.awselect .back_face ul li a {
  padding: 11px 20px !important;
  float: left;
  border-bottom: 1px solid #ebecf1;
  font-family: "Poppins", sans-serif;
}

.awselect .back_face ul li a:hover {
  background: #f5f9fc;
  color: #007BFF;
}

.awselect .awselect-img {
  vertical-align: middle;
  border-style: none;
  width: 20px;
  padding-bottom: 1px;
  margin-right: 1rem;
}

.awselect.placeholder_animate2 .front_face .placeholder {
  color: #728096;
}

.awselect_bg {
  position: relative;
}
.awselect_bg .animate {
  opacity: 0 !important;
}
.text-required {
    font-size: 8px;
    position: absolute;
    top: 1px;
    margin-left: 2px;
    color: red;
}
.card-title-title {
    text-transform: capitalize;
    font-weight: 700;
    font-size: 14px;
}
.cell-box {
    border-radius: 35px;
    padding: 3px 20px;
    color: #fff;
    font-weight: 700;
}
.support-header {
    position: absolute;
    right: 0;
    top: 2rem;
    margin-right: 1.2rem;
    background: rgba(0, 188, 126, 0.1);
    color: #00bc7e;
}
#support-messages-box {
    min-height: 600px;
    border-radius: 8px;
    height: 100%;
    width: 100%;
}
.background-color {
    background-color: #f5f9fc;
}
#support-messages-box .support-message {
    padding: 1rem;
    float: left;
    width: 90%;
    border-radius: 8px;
    box-shadow: 0 1px 8px rgb(179 186 212 / 70%) !important;
}
#support-messages-box .support-message p span {
    float: right;
    color: #FF9D00;
}
#support-messages-box .support-response {
    float: right;
    background-color: #E1F0FF !important;
}
</style>
@endsection

@section('content')
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
    <div class="col-lg-8 col-md-8 col-sm-12 offset-md-2 offset-lg-2 mt-3">
        <div class="card overflow-hidden border-0">
            <div class="card-header block">
                <p class="card-title-title mb-4">Ticket Subject: <span class="text-info">{{ $ticket->subject }}</span></p>
                <p class="card-title-title">Ticket ID: <span class="text-info">{{ $ticket->ticket_id }}</span></p>
                <span class="cell-box mt-xl-0 mt-4 support-header support-{{ strtolower($ticket->status) }}">{{ $ticket->status }}</span>
            </div>
            <div class="card-body pt-2">	
                <div class="row">	
                    <div class="background-color p-4" id="support-messages-box">
                        @foreach ($messages as $message)
                            @if ($message->user->user_type != 'Super Admin')
                                <div class="bg-white support-message mb-3">
                                    <p class="font-weight-bold" style="font-size:14px;"><i class="fa-sharp fa-solid fa-calendar-days mr-2"></i>{{ date_format($message->created_at, 'd M Y H:i A') }} <span>User Message</span></p>
                                    <p class="mb-1" style="font-size:17px;">{{ $message->message }}</p>
                                    @if ($message->attachment)
                                        <p class="font-weight-bold mb-1" style="font-size:14px;">Attachment</p>
                                        <a class="font-weight-bold text-primary" style="font-size:14px;" href="{{ asset('uploads/support/'.$message->attachment) }}">View Attached Image</a>
                                    @endif										
                                </div>
                            @else
                                <div class="bg-white support-message support-response mb-3">
                                    <p class="font-weight-bold" style="font-size:14px;"><i class="fa-sharp fa-solid fa-calendar-days mr-2"></i>{{ date_format($message->created_at, 'd M Y H:i A') }} <span class="text-primary">Admin Response</span></p>
                                    <p class="mb-1" style="font-size:17px;">{{ $message->message }}</p>
                                    @if ($message->attachment)
                                        <p class="font-weight-bold mt-3 mb-1" style="font-size:14px;">Attachment</p>
                                        <a class="font-weight-bold text-primary" style="font-size:14px;" href="{{ asset('uploads/support/'.$message->attachment) }}">View Attached Image</a>
                                    @endif	
                                </div>
                            @endif
                        @endforeach						
                    </div>
                </div>

                <form action="{{ route('admin.support.response', ['ticket_id' => $ticket->ticket_id]) }}" method="post" enctype="multipart/form-data">
                    @csrf
                    <div class="row pt-5">
                        <div class="col-lg-12 col-md-12 col-sm-12">				
                            <div class="input-box">	
                                <h6 class="font-weight-bold">Ticket Status: <span class="text-required"><i class="fa-solid fa-asterisk"></i></span></h6>
                                <select id="response_status" name="response_status" data-placeholder="Select Ticket Status:">			
                                    <option value="Pending" selected>Pending</option>
                                    <option value="Replied">Replied</option>
                                    <option value="Resolved">Resolved</option>										
                                    <option value="Closed">Closed</option>
                                </select>
                            </div> 							
                        </div>
                    </div>

                    <div class="row pt-3">
                        <div class="col-12">
                            <div class="input-box">
                                <h6 class="font-weight-bold">Response : <span class="text-required"><i class="fa-solid fa-asterisk"></i></h6>
                                <textarea class="form-control" name="message" id="reply" rows="6" placeholder="Enter your reply message here..."></textarea> 
                            </div>	

                            <div class="input-box mt-3">
                                <h6 class="font-weight-bold">Attach File</h6>
                                <div class="input-group file-browser">		
                                    <input type="file" class="form-control border-right-0 browse-file" name="attachment">
                                </div>	
                                <span class="text-muted" style="font-size:12px;">JPG | JPEG | PNG | Max 5MB</span>
                            </div>					
                        </div>

                        <div class="col-12 text-center">
                            <!-- SAVE CHANGES ACTION BUTTON -->
                            <div class="border-0 mb-2">
                                <a href="{{ url('admin/support-requests') }}" class="btn btn-danger mr-2">Back</a>
                                @if(Auth::user()->user_type == "Demo")
                                <button type="button" class="btn btn-primary ToastrButton">Reply</button>
                                @else
                                <button type="submit" class="btn btn-primary">Reply</button>	
                                @endif
                            </div>
                        </div>							
                    </div>	
                </form>
            </div>
        </div>
    </div>
</div>
@endsection

@section('script')
<script src="{{asset('assets/plugins/awselect/awselect.min.js')}}"></script>
<script type="text/javascript">
    $('#support-category').awselect(); 
    $('#response_status').awselect(); 
</script>
@endsection