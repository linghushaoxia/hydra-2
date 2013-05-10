/*
 * Copyright jd
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.jd.bdp.hydra.hbase.service.impl;

import com.jd.bdp.hydra.Annotation;
import com.jd.bdp.hydra.Span;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTablePool;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yfliuyu
 * Date: 13-4-12
 * Time: 下午4:19
  */
public class HbaseUtils {
    public static HTablePool POOL;
    public static Configuration conf = HBaseConfiguration.create(new Configuration());
    public static final String duration_index = "duration_index";
    public static final String duration_index_family_column = "trace";
    public static final String ann_index = "annotation_index";
    public static final String ann_index_family_column = "trace";
    public static final String TR_T = "trace";
    public static final String trace_family_column = "span";
    public static final String DUBBO_EXCEPTION = "dubbo.exception";

    static {
//        conf.set("hbase.zookeeper.quorum", "boss,emp1,emp2");
//        conf.set("hbase.client.retries.number", "3");
        POOL = new HTablePool(conf, 2);
    }



    Annotation getSsAnnotation(List<Annotation> alist){
        for(Annotation a : alist){
            if(StringUtils.endsWithIgnoreCase("ss",a.getValue())){
                return a;
            }
        }
        return null;
    }

    Annotation getSrAnnotation(List<Annotation> alist){
        for(Annotation a : alist){
            if(StringUtils.endsWithIgnoreCase("sr",a.getValue())){
                return a;
            }
        }
        return null;
    }

    long byteArray2Long(byte[] value){
        long s = 0;
        long s0 = value[0] & 0xff;
        long s1 = value[1] & 0xff;
        long s2 = value[2] & 0xff;
        long s3 = value[3] & 0xff;
        long s4 = value[4] & 0xff;
        long s5 = value[5] & 0xff;
        long s6 = value[6] & 0xff;
        long s7 = value[7] & 0xff;
        s1 <<= 8;
        s2 <<= 8*2;
        s3 <<= 8*3;
        s4 <<= 8*4;
        s5 <<= 8*5;
        s6 <<= 8*6;
        s7 <<= 8*7;
        s = s0|s1|s2|s3|s4|s5|s6|s7;
        return s;
    }


    byte[] long2ByteArray(Long value){
        long v = value.longValue();
        byte[] b = new byte[8];
        for(int i = 0 ; i < b.length;i++){
            b[i] = new Long(v & 0xff).byteValue();
            v = v >> 8;
        }
        return b;
    }

    boolean isTopAnntation(Span span){
        List<Annotation> alist = span.getAnnotations();
        boolean isfirst = false;
        for(Annotation a : alist){
            if(StringUtils.endsWithIgnoreCase("cs",a.getValue())){
                isfirst = true;
            }
        }
        return isfirst;
    }

    Annotation getCsAnnotation(List<Annotation> alist){
        for(Annotation a : alist){
            if(StringUtils.endsWithIgnoreCase("cs", a.getValue())){
                return a;
            }
        }
        return null;
    }

    Annotation getCrAnnotation(List<Annotation> alist){
        for(Annotation a : alist){
            if(StringUtils.endsWithIgnoreCase("cr",a.getValue())){
                return a;
            }
        }
        return null;
    }

}
