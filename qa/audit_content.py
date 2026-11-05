#!/usr/bin/env python3
"""Verify preservation against the pinned upstream commit; run from this worktree."""
from pathlib import Path
import hashlib, json, re, subprocess, zipfile, struct
ROOT=Path(__file__).resolve().parents[1]
UPSTREAM='5778ed081dfb18656f04cb1ba91220bf87ea2bba'
def git(*args):return subprocess.check_output(['git','-C',str(ROOT),*args])
files=git('ls-tree','-r','--name-only',UPSTREAM).decode().splitlines()
checks=[]
def check(name,ok,detail):
 checks.append(ok);print(('PASS ' if ok else 'FAIL ')+name+' :: '+str(detail))
for file in files:
 if file.endswith('.png') or '/lang/' in file or '/loot_table/' in file or '/worldgen/' in file:
  check('preserved/'+file,(ROOT/file).read_bytes()==git('show',UPSTREAM+':'+file),'byte-for-byte upstream')
for registry,token in [('ModItems','ITEMS'),('ModBlocks','BLOCKS')]:
 f='src/main/java/cn/foggyhillside/ends_delight/registry/'+registry+'.java'
 before=set(re.findall(token+r'.register\("([^"]+)"',git('show',UPSTREAM+':'+f).decode()))
 after=set(re.findall(token+r'.register\("([^"]+)"',(ROOT/f).read_text()))
 check('registry/'+registry,before==after,{'count':len(after),'removed':sorted(before-after),'added':sorted(after-before)})
recipes={f for f in files if '/recipe/' in f and f.endswith('.json')}
now={str(f.relative_to(ROOT)) for f in (ROOT/'src/main/resources/data/ends_delight/recipe').rglob('*.json')}
check('recipe-ids',recipes==now,len(now))
for file in files:
 if '/loot_modifiers/' in file and '/ends_delight/' in file:
  old=json.loads(git('show',UPSTREAM+':'+file));new=json.loads((ROOT/file).read_text());new.pop('priority',None)
  check('loot-policy/'+file,old==new,'conditions, outputs, modifier type preserved')
for f in (ROOT/'src/main/resources').rglob('*.json'):json.loads(f.read_text(encoding='utf-8-sig'))
check('json-syntax',True,'all resource JSON decoded')
jar=next(p for p in (ROOT/'build/libs').glob('*.jar') if not p.name.endswith('-qa.jar'))
with zipfile.ZipFile(jar) as z:
 names=z.namelist();classes=[n for n in names if n.endswith('.class')]
 check('bytecode-java25',all(struct.unpack('>H',z.read(n)[6:8])[0]==69 for n in classes),len(classes))
 check('no-bundled-qa-or-fd',not any('/qa/' in n or n.startswith('vectorwing/') or n.endswith('-qa.jar') for n in names),'standalone End’s Delight only')
 check('license', 'LICENSE' in names,'MIT included')
 check('item-definitions',len([n for n in names if n.startswith('assets/ends_delight/items/') and n.endswith('.json')])==63,'63 definitions')
 print('JAR',jar,'SHA256',hashlib.sha256(jar.read_bytes()).hexdigest())
print('SUMMARY',sum(checks),'passed;',len(checks)-sum(checks),'failed')
raise SystemExit(0 if all(checks) else 1)
