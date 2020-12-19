#!/usr/bin/python

import sys
import json
import os.path

def main():
    global_meta_json = json.loads('[]')
    exists = os.path.exists(sys.argv[1])
    if exists == True:
        with open(sys.argv[1], 'r', encoding='utf8') as global_meta:
            global_meta_json = json.loads(global_meta.read())


    with open(sys.argv[2], 'r', encoding='utf8') as modified_meta:
        modified_meta_json = json.loads(modified_meta.read())
        modified_meta_json['path'] = sys.argv[3]
        found = False
        for i in global_meta_json:
            if i['uri'] == modified_meta_json['uri']:
                i = modified_meta_json
                found = True
                break

        if found == False:
            global_meta_json.append(modified_meta_json)

        print(json.dumps(global_meta_json, sort_keys=True, indent=4, ensure_ascii=False))

main()