//公共变量
var $box = $('#indexBody');
/**
 * 用户名
 * @type {string}
 */
var xClientUsername = sessionStorage.getItem("x-client-username");
/**
 * 用户token
 * @type {string}
 */
var xClientToken = sessionStorage.getItem("x-client-token");

//其他公共变量组合
$.sys = {
    modal : function(html){
        var $modal = $('#indexModal');
        $modal.find('.modal-body').html(html);
        $modal.modal('show');
    },
    url : {
        client : function (url) {
            return '/client'+url;
        },
        provider : function (url) {
            return '/provider'+url;
        }
    },
    //公共bootstrap-table 参数
    BSTableParams : function (params) {
        var defauts = {
            method: 'POST',
            dataType: 'json',
            contentType: "application/json",
            cache: false,
            striped: true,    //是否显示行间隔色
            sidePagination: "server",    //分页方式：client客户端分页，server服务端分页（*）
            search: true,
            searchTimeOut : 0,
            // showSearchButton: true,
            showRefresh: true,
            height: $box.height() - 10,
            toolbar : '#toolbar',
            showColumns: true,
            pagination: true,
            queryParamsType: 'limit',
            minimumCountColumns: 2, //最少允许的列数
            pageNumber: 1,        //初始化加载第一页，默认第一页
            pageSize: 20,            //每页的记录行数（*）
            pageList: [10, 20, 50, 100],       //可供选择的每页的行数（*）
            uniqueId: "id",      //每一行的唯一标识，一般为主键列
            showExport: true,
            exportDataType: 'all',
            exportTypes: ['xlsx', 'excel','csv', 'txt'], //'json', 'xml','doc'
            totalField : 'total',
            dataField : 'records',
            responseHandler: function (res) {
                return res.model;
            },
            //数据加载完成后触发
            // onLoadSuccess : function(){},
        };
        return $.extend({},defauts,params);
    }
};

$(function () {
    $('title').text('欢迎您，'+$('#indexTitle').data('name'));
    var $menu = $('#indexMenu');
    $menu.xTree({
        headerAuto : true,
        single : true,
        openFirstMenu : true,
        headerClick : function(){

        },
        itemClick : function (data) {
            $.ajaxPrefix({
                url : $.sys.url.client(data.url),
                success : function (res) {
                    $('#indexBody').html(res);
                }
            });
        }
    });
    //默认加载首页
    $menu.find('>div:first').trigger('click');
});