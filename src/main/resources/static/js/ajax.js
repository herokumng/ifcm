/**
 * ajax 공통모듈
 * ------------------

 */

/**
 * Ajax 콜 함수
 * @param _url: api 주소
 * @param _param: parameters
 * @param _callback: 성공시 callback 함수
 * @param _httpMethod: GET, POST, PUT, DELETE
 * @param _errorMsg
 */
const ajax = function(_url, _param, _callback, _httpMethod, _errMsg) {

      $("div.spanner").addClass("show");
      $("div.overlay").addClass("show")

    if (!_httpMethod) _httpMethod = 'GET'

    let request = $.ajax({
        url: _url,
        method: _httpMethod,
        data: _param,
        dataType: 'json',
        contentType: 'application/json',
    });

    //콜백함수
    if (_callback) request.done(_callback);

    request.fail(function (jqXHR, textStatus) {
        alert(_errMsg + " " + textStatus);
    });

    request.always(()=>{
        $("div.spanner").removeClass("show");
        $("div.overlay").removeClass("show")
    })
}

const ajax2 = function(_url, _param, _callback, _httpMethod, _errMsg) {

    $("div.spanner").addClass("show");
    $("div.overlay").addClass("show")

    if (!_httpMethod) _httpMethod = 'GET'

    let request = $.ajax({
        url: _url,
        method: _httpMethod,
        data: _param,
        processData : false,
        contentType : false,
        enctype: "multipart/form-data",
    });

    //콜백함수
    if (_callback) request.done(_callback);

    request.fail(function (jqXHR, textStatus) {
        alert(_errMsg + " " + textStatus);
    });

    request.always(()=>{
        $("div.spanner").removeClass("show");
        $("div.overlay").removeClass("show")
    })
}