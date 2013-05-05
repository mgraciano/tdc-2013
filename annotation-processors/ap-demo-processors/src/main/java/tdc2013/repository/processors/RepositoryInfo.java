/*
 * Copyright (c) 2013, Klaus Boeing & Michel Graciano.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the project's authors nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS AND/OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package tdc2013.repository.processors;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RepositoryInfo {

    private String packageName;
    private String interfaceName;
    private String interfacePath;
    private List<MethodInfo> methods = new ArrayList<>();

    public String getDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public void addMethod(MethodInfo methodInfo) {
        methods.add(methodInfo);
    }

    public List<MethodInfo> getMethods() {
        return methods;
    }

    public String getEntityName() {
        return packageName.concat(".").concat(interfaceName.replaceAll("Repository", ""));
    }

    public RepositoryInfo(String packageName, String interfaceName, String interfacePath) {
        this.packageName = packageName;
        this.interfaceName = interfaceName;
        this.interfacePath = interfacePath;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public String getInterfacePath() {
        return interfacePath;
    }

    public String getPackageName() {
        return packageName;
    }

    public final class MethodInfo {

        private String name;
        private String returnType;
        private Map<String, String> parameters = new LinkedHashMap<>();

        public MethodInfo(String name, String returnType) {
            this.name = name;
            this.returnType = returnType;
        }

        public String getName() {
            return name;
        }

        public String getQuery() {
            return new QueryMethodBuilder(getEntityName(), name).build();
        }

        public void addParameter(String name, String type) {
            parameters.put(name, type);
        }

        public Collection<String> getParameters() {
            return parameters.keySet();
        }

        public boolean isReturnTypeCollection() {
            return returnType.matches("java.util.(Collection|List|Set)");
        }

        public String getParametersString() {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                sb.append(" ");
                sb.append(entry.getValue());
                sb.append(" ");
                sb.append(entry.getKey());

                if (++i < parameters.size()) {
                    sb.append(",");
                }
            }

            return sb.toString();
        }

        public String getReturnType() {
            return returnType;
        }
    }
}
