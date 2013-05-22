<#--

    Copyright (c) 2013, Klaus Boeing & Michel Graciano.
    All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions are met:

    Redistributions of source code must retain the above copyright notice, this
    list of conditions and the following disclaimer.

    Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions and the following disclaimer in the documentation
    and/or other materials provided with the distribution.

    Neither the name of the project's authors nor the names of its contributors
    may be used to endorse or promote products derived from this software without
    specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
    AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
    IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
    ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS AND/OR CONTRIBUTORS
    BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
    CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
    SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
    INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
    CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
    ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
    POSSIBILITY OF SUCH DAMAGE.

-->
package ${packageName};

import javax.inject.Named;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.inject.Inject;

@Named
@Stateless
@javax.annotation.Generated(value = "tdc2013.repository.processors", date = "${date}")
public class ${interfaceName}Impl implements ${interfacePath}{

    @Inject
    private EntityManager em;

<#list methods as m>

<#if m.name == 'save'>
    @Override
    public void ${m.name}(${entityName} entity){
        em.merge(entity);
    }
<#else>
    @Override
    public ${m.returnType} ${m.name}(${m.parametersString}){

        TypedQuery<${entityName}> query = em.createQuery("${m.query}", ${entityName}.class);
                
        <#list m.parameters as item>
        query.setParameter(${item_index + 1}, ${item});
        </#list>

        <#if m.returnTypeCollection>
        return query.getResultList();
        <#else>
        return query.getSingleResult();
        </#if>
    }
</#if>
</#list>

}