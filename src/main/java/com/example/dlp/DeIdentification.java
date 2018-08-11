/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.dlp;

import java.util.List;

import com.google.cloud.dlp.v2.DlpServiceClient;
import com.google.privacy.dlp.v2.CharacterMaskConfig;
import com.google.privacy.dlp.v2.ContentItem;
import com.google.privacy.dlp.v2.DeidentifyConfig;
import com.google.privacy.dlp.v2.DeidentifyContentRequest;
import com.google.privacy.dlp.v2.DeidentifyContentResponse;
import com.google.privacy.dlp.v2.InfoType;
import com.google.privacy.dlp.v2.InfoTypeTransformations;
import com.google.privacy.dlp.v2.InfoTypeTransformations.InfoTypeTransformation;
import com.google.privacy.dlp.v2.InspectConfig;
import com.google.privacy.dlp.v2.PrimitiveTransformation;
import com.google.privacy.dlp.v2.ProjectName;

public class DeIdentification {

  public static String deIdentifyWithMask(String string, DlpServiceClient dlpServiceClient,  List<InfoType> infoTypes, String projectId) {
    	
	 
         Character maskingCharacter= '*';
         int numberToMask =Integer.parseInt("0");
        
      ContentItem contentItem = ContentItem.newBuilder().setValue(string).build();

      CharacterMaskConfig characterMaskConfig = CharacterMaskConfig.newBuilder().setMaskingCharacter(maskingCharacter.toString()).setNumberToMask(numberToMask).build();

      // Create the deidentification transformation configuration
      PrimitiveTransformation primitiveTransformation = PrimitiveTransformation.newBuilder().setCharacterMaskConfig(characterMaskConfig).build();

      InfoTypeTransformation infoTypeTransformationObject =
          InfoTypeTransformation.newBuilder()
              .setPrimitiveTransformation(primitiveTransformation)
              .build();

      InfoTypeTransformations infoTypeTransformationArray =
          InfoTypeTransformations.newBuilder()
              .addTransformations(infoTypeTransformationObject)
              .build();

      InspectConfig inspectConfig =
          InspectConfig.newBuilder()
              .addAllInfoTypes(infoTypes)
              .build();

      DeidentifyConfig deidentifyConfig =
          DeidentifyConfig.newBuilder()
              .setInfoTypeTransformations(infoTypeTransformationArray)
              .build();

      // Create the deidentification request object
      DeidentifyContentRequest request =
          DeidentifyContentRequest.newBuilder()
              .setParent(ProjectName.of(projectId).toString())
              .setInspectConfig(inspectConfig)
              .setDeidentifyConfig(deidentifyConfig)
              .setItem(contentItem)
              .build();

      // Execute the deidentification request
      DeidentifyContentResponse response = dlpServiceClient.deidentifyContent(request);

      String result = response.getItem().getValue();
      System.out.println(result);
      return result;
  
  }


  
}
