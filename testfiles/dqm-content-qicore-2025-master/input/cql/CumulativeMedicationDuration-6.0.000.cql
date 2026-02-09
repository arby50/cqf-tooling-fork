library CumulativeMedicationDuration version '6.0.000'

using QICore version '6.0.0'

include FHIRHelpers version '4.4.000' called FHIRHelpers

context Patient

define fluent function medicationRequestPeriod(Request "MedicationRequest"):
  Request.dispenseRequest.validityPeriod
