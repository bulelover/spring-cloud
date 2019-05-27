/**
 * 扩展静态工具类
 */
jQuery.extend({
    //默认项目请求根路径
    clientPath : '/client',
    //登录认证根路径
    authPath : '/auth',
    //判断非空
    isEmpty : function (data) {
        return (data == "" || data == undefined || data == null) ? true : false;
    },
    //去除两边空格判断非空
    isBlank : function(data){
        if(this.isNull(data)){
            return true;
        }
        return this.isEmpty($.trim(data));

    },
    //判断非null
    isNull : function (data) {
        return (data == undefined || data == null) ? true : false;
    },

    ajaxPrefix : function (prefix,options) {
        if ( typeof prefix === "object" ) {
            options = prefix;
            prefix = this.clientPath;
        }
        options.url = prefix+options.url;
        $.ajax(options);
    },

    /**
     * 默认使用POST JSON形式发送请求，ajax参数可被覆盖 携带token参数
     * @param prefix 请求前缀，默认使用clientPath变量初始值
     * @param options
     */
    ajaxJson : function (prefix,options) {
        if ( typeof prefix === "object" ) {
            options = prefix;
            prefix = this.clientPath;
        }
        options.url = prefix+options.url;

        options.extend({
            type : 'POST',
            contentType: "application/json"
        });

        var xClientUsername = sessionStorage.getItem("x-client-username");
        var xClientToken = sessionStorage.getItem("x-client-token");
        if(options.data && options.data.length>0){
            options.data['username'] = xClientUsername;
            options.data['token'] = xClientToken;
        }else{
            options.data = {
                username : xClientUsername,
                token : xClientToken
            }
        }
        options.data = JSON.stringify(options.data);
        $.ajax(options);
    }
});

/**
 * 扩展对象工具类
 */
+(function ($) {

    //序列化表单为Json格式数据
    $.fn.serializeJson = function () {
        var o = {};
        var a = this.serializeArray();

        $.each(a, function () {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]]
                }
                o[this.name].push(this.value || '')
            } else {
                o[this.name] = this.value || ''
            }
        });

        return o;
    };
    $.fn.serializeJsonSfy = function () {
        return JSON.stringify(this.serializeJson());
    };

}(jQuery));


/**
 * 扩展String方法
 */
$.extend(String.prototype, {
    isInteger: function() {
        return (new RegExp(/^\d+$/).test(this))
    },
    isNumber: function(value, element) {
        return (new RegExp(/^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/).test(this))
    },
    trim: function() {
        return this.replace(/(^\s*)|(\s*$)|\r|\n/g, '')
    },
    replaceAll: function(os, ns) {
        return this.replace(new RegExp(os,'gm'), ns)
    },
    toBool: function() {
        return (this.toLowerCase() === 'true')
    },
    toJson: function() {
        var json = this

        try {
            if (typeof json == 'object') json = json.toString()
            if (!json.trim().match("^\{(.+:.+,*){1,}\}$")) return this
            else return JSON.parse(this)
        } catch (e) {
            return this
        }
    },
    toObj: function() {
        var obj = null
        try {
            obj = (new Function('return '+ this))()
        } catch (e) {
            obj = this;
        }
        return obj
    }
});