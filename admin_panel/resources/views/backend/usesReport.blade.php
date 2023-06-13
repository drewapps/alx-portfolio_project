@extends('layouts.master')

@section('extra_css')
<style type="text/css">

</style>
@endsection

@section('content')
<div class="row">
    <div class="col-lg-3 col-xs-6">
        <a href="#">
            <div class="info-box" style="border-radius: 7px;background-color: #28C76F;color:white;">
                <span class="info-box-icon ml-3"><img src="{{asset('assets/images/icon/User.svg')}}"/></span>
                <div class="info-box-content text-right">
                    <span class="info-box-text mt-2"><b>This Month Word Generated</b></span>
                    <span class="info-box-number mb-2"><h2>{{$this_month_word}}</h2></span>
                </div>
            </div>
        </a>
    </div>

    <div class="col-lg-3 col-xs-6">
        <a href="#">
            <div class="info-box" style="border-radius: 7px;background-color: #FF3EA6;color:white;">
                <span class="info-box-icon ml-3"><img src="{{asset('assets/images/icon/Category.svg')}}"/></span>
                <div class="info-box-content text-right">
                    <span class="info-box-text mt-2"><b>This Month Image Generated</b></span>
                    <span class="info-box-number mb-2"><h2>{{$this_month_image}}</h2></span>
                </div>
            </div>
        </a>
    </div>

    <div class="col-lg-3 col-xs-6">
        <a href="#">
            <div class="info-box" style="border-radius: 7px;background-color: #1E9FF2;color:white;">
                <span class="info-box-icon ml-3"><img src="{{asset('assets/images/icon/Festival.svg')}}"/></span>
                <div class="info-box-content text-right">
                    <span class="info-box-text mt-2"><b>Total Word Generated</b></span>
                    <span class="info-box-number mb-2"><h2>{{$all_word}}</h2></span>
                </div>
            </div>
        </a>
    </div>

    <div class="col-lg-3 col-xs-6">
        <a href="#">
            <div class="info-box" style="border-radius: 7px;background-color: #FF9F43;color:white;">
                <span class="info-box-icon ml-3"><img src="{{asset('assets/images/icon/Business.svg')}}"/></span>
                <div class="info-box-content text-right">
                    <span class="info-box-text mt-2"><b>Total Image Generated</b></span>
                    <span class="info-box-number mb-2"><h2>{{$all_image}}</h2></span>
                </div>
            </div>
        </a>
    </div>
</div>

<div class="row">
    <div class="col-md-12">
        <div class="row">
            <div class="col-md-6">
                <div class="card card-light">
                    <div class="card-header text-primary border-bottom">
                        <h5 class="float-left"><b>Daily Word Generated</b></h5>
                    </div>
                    <div class="card-body">
                        <canvas id="day_word_generated" style="width:100%;max-width:800px"></canvas>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="card card-light">
                    <div class="card-header text-primary border-bottom">
                        <h5 class="float-left"><b>Daily Image Generated</b></h5>
                    </div>
                    <div class="card-body">
                        <canvas id="day_image_generated" style="width:100%;max-width:800px"></canvas>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-xl-6">
        <div class="card card-primary">
            <div class="card-header text-center border-bottom">
                <h5><b>Today Generated Document</b></h5>
            </div>
            <ul class="list-group list-group-flush">
                @foreach($today_document as $d)
                    <li class="list-group-item d-flex justify-content-between">
                        <div class="d-flex flex-row">
                            <img src="{{asset('assets/images/social/'.$d->templates->image)}}" class="my-auto" style="height:40px;width:40px;">
                            <div class="d-flex flex-column ml-3 my-auto">
                                <span class="d-block" style="font-size:17px;">{{$d->name}}</span>
                                <span class="text-muted" style="font-size:15px;">{{$d->user->name}} / {{$d->templates->type}} / {{$d->document_word}} Word</span>
                            </div>
                        </div>
                        <div class="d-flex flex-column">
                            <span class="d-block my-auto"><a href="{{url('admin/user/'.$d->user->id) }}" class="text-primary" style="font-size:18px;">View</a></span>
                        </div>
                    </li>
                @endforeach
            </ul>
            <div class="card-footer text-center">
                <a href="{{url('admin/today-generated-document')}}" class="btn btn-primary">View More</a>
            </div>
        </div>
    </div>

    <div class="col-xl-6">
        <div class="card card-primary">
            <div class="card-header text-center border-bottom">
                <h5><b>Today Generated Image</b></h5>
            </div>
            <ul class="list-group list-group-flush">
                @foreach($today_image as $i)
                <li class="list-group-item d-flex justify-content-between">
                    <div class="d-flex flex-row">
                        <img src="{{asset('uploads/aiImage/'.$i->image)}}" class="my-auto" style="height:40px;width:40px;">
                        <div class="d-flex flex-column ml-2 my-auto">
                            <span class="d-block">{{(strlen($i->description) >= 50)?substr_replace($i->description, "...", 50):$i->description}}</span>
                            <span class="text-muted" style="font-size:15px;">{{$i->user->name}} / {{$i->resolution}} Pixel</span>
                        </div>
                    </div>
                    <div class="d-flex flex-column">
                        <span class="d-block my-auto"><a href="{{asset('uploads/aiImage/'.$i->image)}}" target="_blank" class="text-primary" style="font-size:18px;">View</a></span>
                    </div>
                </li>
                @endforeach
            </ul>
            <div class="card-footer text-center">
                <a href="{{url('admin/today-generated-image')}}" class="btn btn-primary">View More</a>
            </div>
        </div>
    </div>
</div>
@endsection

@section("script")
<script src="{{ asset('assets/js/cdn/Chart.bundle.min.js')}}"></script>
<script src="{{ asset('assets/js/cdn/canvasjs.min.js')}}"></script>
<script>
$(document).ready(function(){
    var userMonthlyData = JSON.parse(`<?php echo $monthly_usage_word; ?>`);
    var userMonthlyDataset = Object.values(userMonthlyData);
    var userMonthlyDataImage = JSON.parse(`<?php echo $monthly_usage_image; ?>`);
    var userMonthlyDataImageset = Object.values(userMonthlyDataImage);
    let delayed1;

    new Chart("day_word_generated", {
        type: "bar",
        data: {
            labels:['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', '31'],
            datasets: [{
                label: 'Words Generated',
                data: userMonthlyDataset,
                backgroundColor: '#007bff',
                borderWidth: 1,
                borderRadius: 20,
                barPercentage: 0.7,
                fill: true
            }]
        },
        options: {
            maintainAspectRatio: false,
            legend: {
                display: false,
                labels: {
                    display: false
                }
            },
            responsive: true,
            animation: {
                onComplete: () => {
                    delayed1 = true;
                },
                delay: (context) => {
                    let delay = 0;
                    if (context.type === 'data' && context.mode === 'default' && !delayed1) {
                        delay = context.dataIndex * 50 + context.datasetIndex * 5;
                    }
                    return delay;
                },
            },
            scales: {
                y: {
                    stacked: true,
                    ticks: {
                        beginAtZero: true,
                        font: {
                            size: 10
                        },
                        stepSize: 50000,
                    },
                    grid: {
                        color: '#ebecf1',
                        borderDash: [3, 2]                            
                    }
                },
                x: {
                    stacked: true,
                    ticks: {
                        font: {
                            size: 10
                        }
                    },
                    grid: {
                        color: '#ebecf1',
                        borderDash: [3, 2]                            
                    }
                }
            },
            plugins: {
                tooltip: {
                    cornerRadius: 10,
                    xPadding: 10,
                    yPadding: 10,
                    backgroundColor: '#000000',
                    titleColor: '#FF9D00',
                    yAlign: 'bottom',
                    xAlign: 'center',
                },
                legend: {
                    position: 'bottom',
                    labels: {
                        boxWidth: 10,
                        font: {
                            size: 10
                        }
                    }
                }
            }
        }
    });

    new Chart("day_image_generated", {
        type: "bar",
        data: {
            labels:['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', '31'],
            datasets: [{
                label: 'Image Generated',
                data: userMonthlyDataImageset,
                backgroundColor: '#F90611',
                borderWidth: 1,
                borderRadius: 20,
                barPercentage: 0.7,
                fill: true
            }]
        },
        options: {
            maintainAspectRatio: false,
            legend: {
                display: false,
                labels: {
                    display: false
                }
            },
            responsive: true,
            animation: {
                onComplete: () => {
                    delayed1 = true;
                },
                delay: (context) => {
                    let delay = 0;
                    if (context.type === 'data' && context.mode === 'default' && !delayed1) {
                        delay = context.dataIndex * 50 + context.datasetIndex * 5;
                    }
                    return delay;
                },
            },
            scales: {
                y: {
                    stacked: true,
                    ticks: {
                        beginAtZero: true,
                        font: {
                            size: 10
                        },
                        stepSize: 50000,
                    },
                    grid: {
                        color: '#ebecf1',
                        borderDash: [3, 2]                            
                    }
                },
                x: {
                    stacked: true,
                    ticks: {
                        font: {
                            size: 10
                        }
                    },
                    grid: {
                        color: '#ebecf1',
                        borderDash: [3, 2]                            
                    }
                }
            },
            plugins: {
                tooltip: {
                    cornerRadius: 10,
                    xPadding: 10,
                    yPadding: 10,
                    backgroundColor: '#000000',
                    titleColor: '#FF9D00',
                    yAlign: 'bottom',
                    xAlign: 'center',
                },
                legend: {
                    position: 'bottom',
                    labels: {
                        boxWidth: 10,
                        font: {
                            size: 10
                        }
                    }
                }
            }
        }
    });
});
</script>
@endsection