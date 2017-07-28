package my.fancy.organization.beans

case class Request(existing_col_name: String, new_col_name: String, new_data_type: String, date_expression: Option[String])
