/******************************************************************************
 *                                                                            *
 * Copyright 2018 Jan Henrik Weinstock                                        *
 *                                                                            *
 * Licensed under the Apache License, Version 2.0 (the "License");            *
 * you may not use this file except in compliance with the License.           *
 * You may obtain a copy of the License at                                    *
 *                                                                            *
 *     http://www.apache.org/licenses/LICENSE-2.0                             *
 *                                                                            *
 * Unless required by applicable law or agreed to in writing, software        *
 * distributed under the License is distributed on an "AS IS" BASIS,          *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   *
 * See the License for the specific language governing permissions and        *
 * limitations under the License.                                             *
 *                                                                            *
 ******************************************************************************/

package org.vcml.explorer.ui.handlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.vcml.explorer.ui.parts.SystemCPart;
import org.vcml.explorer.ui.services.ISessionService;
import org.vcml.session.Module;

public class InspectModuleHandler {

    public static final String STACK_ID = "org.vcml.explorer.ui.partstack.middle";

    static final String BUNDLE_URI = "bundleclass://org.vcml.explorer.ui";

    private Map<String, String> parts = new HashMap<String, String>();

    public void registerPartContribution(String kind, Class<?> clazz) {
        String uri = BUNDLE_URI + "/" + clazz.getName();
        parts.put(kind, uri);
    }

    public InspectModuleHandler() {
        registerPartContribution(Module.KIND_VCML_BUS, SystemCPart.class);
        registerPartContribution(Module.KIND_VCML_UART, SystemCPart.class);
        registerPartContribution(Module.KIND_VCML_PROCESSOR, SystemCPart.class);
    }

    @CanExecute
    public boolean canExecute(ESelectionService selectionService) {
        Object selection = selectionService.getSelection();
        if (!(selection instanceof Module))
            return false;
        Module module = (Module) selection;
        String uri = parts.get(module.getKind());
        return uri != null;
    }

    @Execute
    public void execute(EPartService partService, EModelService modelService, ESelectionService selectionService,
            MApplication application, ISessionService sessionService) {
        Module selection = (Module) selectionService.getSelection();
        String selectionId = sessionService.currentSession() + "/" + selection.getName();

        MPart part = partService.findPart(selectionId);
        if (part == null) {
            part = MBasicFactory.INSTANCE.createPart();
            part.setLabel("SystemC");
            part.setContributionURI(parts.get(selection.getKind()));
            part.setCloseable(true);
            part.setElementId(selectionId);
        }

        List<MPartStack> stacks = modelService.findElements(application, STACK_ID, MPartStack.class, null);
        stacks.get(0).getChildren().add(part);
        partService.showPart(part, PartState.ACTIVATE);
    }

}