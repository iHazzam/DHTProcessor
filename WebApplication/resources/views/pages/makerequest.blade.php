@extends('layouts.app')

@section('content')
    <div class="container">
        <div class="row">
            <div class="col-md-8 col-md-offset-2">
                <div class="panel panel-default">
                    <div class="panel-heading">Make Request</div>

                    <div class="panel-body">
                        <h2>Make Requests:</h2>
                        <p><span>Currently this processor offers 4 tasks. You can...</span>
                            <button type="button" class="btn btn-primary paddme">Find out word count, most common word and average word length from a .txt file</button>
                            <button type="button" class="btn btn-success paddme " >Find out word count, most common word and average word length from a .docx file</button>
                            <button type="button" class="btn btn-warning paddme">Find out the sum, mean and mode of all numbers in a .xlsx file</button>
                            <button type="button" class="btn btn-danger paddme">Find out the average colour of a picture (currently accepts GIF, PNG, JPEG only) (</button>
                        </p>
                    </div>
                </div>
            </div>
        </div>

        {{--<div id="modal{{$teacher->id}}" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">--}}
            {{--<div class="modal-dialog">--}}
                {{--<div class="modal-content">--}}
                    {{--<div class="modal-header">--}}
                        {{--<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;  </button>--}}
                        {{--<h4 class="modal-title" id="myModalLabel">{{$teacher->first_name . " " . $teacher->last_name . " - Music Teacher"}}</h4>--}}
                    {{--</div>--}}
                    {{--<div class="modal-body">--}}

                    {{--</div>--}}
                    {{--<div class="modal-footer">--}}
                        {{--<button id="{{$teacher->id}}" name="{{$teacher->first_name}}" class="btn btn-warning" onclick="openModalTeacher(this.id,this.name)">Request contact details!</button>--}}
                        {{--<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>--}}
                    {{--</div>--}}
                {{--</div>--}}
            {{--</div>--}}
        {{--</div>--}}
    </div>
@endsection
