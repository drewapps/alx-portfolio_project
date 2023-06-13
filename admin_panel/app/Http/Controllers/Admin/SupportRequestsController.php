<?php

namespace App\Http\Controllers\Admin;

use Auth;
use Illuminate\Support\Str;
use Illuminate\Http\Request;
use App\Models\SupportTicket;
use App\Models\SupportMessage;
use App\Http\Controllers\Controller;
use App\Mail\ReplySupportTicketMail;
use Illuminate\Support\Facades\Mail;
use Illuminate\Support\Facades\Validator;

class SupportRequestsController extends Controller
{
    public function index()
    {
        $index['data'] = SupportTicket::latest()->get();
        $index['open_ticket'] = SupportTicket::where('status','Open')->count();
        $index['replied_ticket'] = SupportTicket::where('status','Replied')->count();
        $index['pending_ticket'] = SupportTicket::where('status','Pending')->count();
        $index['resolved_ticket'] = SupportTicket::where('status','Resolved')->count();
        $index['closed_ticket'] = SupportTicket::where('status','Closed')->count();
        return view("backend.SupportRequests", $index);
    }

    public function multi_delete_support_request(Request $request)
    {
        $ids = json_decode($request->get("deleted_ids"), true);
        foreach($ids as $id)
        {
            $ticket = SupportTicket::find($id); 
            if($ticket)
            {
                $ticket->delete();
                SupportMessage::where('ticket_id',$ticket->ticket_id)->delete(); 
            }
        }
        return redirect('admin/support-requests'); 
    }

    public function response(Request $request)
    { 
        $validation = Validator::make($request->all(), [
            'message' => 'required',
            'response_status' => 'required',
            'attachment' => 'nullable|image|mimes:jpeg,png,jpg|max:5048'
        ]);

        if ($validation->fails()) {
            return back()->withErrors($validation)->withInput();
        } 
        else 
        {
            if ($request->response_status == 'Closed' || $request->response_status == 'Resolved') {
                $resolved_on = now();
            } else {
                $resolved_on = null;
            }

            $ticket = SupportTicket::where('ticket_id', $request->ticket_id)->firstOrFail();
            $ticket->status = $request->response_status;
            $ticket->resolved_on = $resolved_on;
            $ticket->save();
    
            $id = SupportMessage::create([
                'message' => htmlspecialchars($request->message),
                'user_id' => Auth::user()->id,
                'ticket_id' => $request->ticket_id,
            ])->id; 

            if ($request->file("attachment") && $request->file('attachment')->isValid()) {
                $this->upload_file($request->file("attachment"),"attachment", $id);
            }

            try
            {
                $message = htmlspecialchars($request->message);
                Mail::to($ticket->user->email)->send(new ReplySupportTicketMail($request->ticket_id,$ticket->user->name,$message));
            }
            catch(\Exception $e)
            {
                return back()->withErrors($e->getmessage())->withInput();
            }

            return redirect('admin/support-requests/'.$request->ticket_id);
        }
    }

    public function show($ticket_id)
    {   
        $ticket = SupportTicket::where('ticket_id', $ticket_id)->firstOrFail();
        $messages = SupportMessage::where('ticket_id', $ticket_id)->get();

        return view('backend.SupportRequestShow', compact('ticket', 'messages'));          
    }

    public function destroy($id)
    {
        $ticket = SupportTicket::where('ticket_id', $id)->firstOrFail(); 
        
        if ($ticket)
        {
            $ticket->delete();
            SupportMessage::where('ticket_id', $id)->delete(); 
        }
        
        return redirect('admin/support-requests');
    }

    private function upload_file($file,$field,$id)
    {
        $destinationPath = './uploads/support';
        $extension = $file->getClientOriginalExtension();
        $fileName = Str::uuid() . '.' . $extension;
        $file->move($destinationPath, $fileName);
        
        $file_data = SupportMessage::find($id);
        $file_data->$field = $fileName;
        $file_data->save();
    }
}
